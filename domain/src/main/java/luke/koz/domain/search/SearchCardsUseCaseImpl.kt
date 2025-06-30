package luke.koz.domain.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.model.CardSearchResult
import luke.koz.domain.repository.CardGalleryRepository

private const val MIN_QUERY_LENGTH_FOR_APPROXIMATE_MATCH = 3

class SearchCardsUseCaseImpl(
    private val cardGalleryRepository: CardGalleryRepository
) : SearchCardsUseCase {

    private val debounceTimeMillis : Long = 500L
    private val minSimilarityThreshold: Double = 0.65

    private var allCards: List<CardGalleryEntry>? = null
    private var normalizedCards: List<Pair<CardGalleryEntry, String>>? = null

    /**
     * Executes a search operation for cards based on the provided [query].
     *
     * Prevents redundant searches by processing the query only if it has changed.
     * Debounce the input query to prevent excessive search requests during rapid typing.
     * Checks if the `List<CardGalleryEntry>` is cached before performing any further logic,
     * if it is not caches it.
     * Normalizes the query (trims whitespace, converts to lowercase).
     *
     * Performs an exact substring match search. If the normalized query length meets a minimum
     * threshold of [MIN_QUERY_LENGTH_FOR_APPROXIMATE_MATCH], it also performs an
     * approximate substring match search.
     *
     * Emits a [CardSearchResult] containing both exact and approximate matches.
     *
     * @param query The user's raw search input.
     * @return A [Flow] of [CardSearchResult] objects, representing the search results.
     * An empty [CardSearchResult] is emitted for empty queries.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(query: String): Flow<CardSearchResult> {
        return flowOf(query)
            /* As per current implementation: this block is aware of most recent query submitted.
             * This conditions the Flow update to perform search logic only per query change,
             * that being: should it receive the same query again (say on recomposition), it won't
             * perform redundant search logic
             */
            .distinctUntilChanged()
            .flatMapLatest { providedQuery ->
                flow {
                    delay(debounceTimeMillis)
                    ensureCardsCached()

                    val normalizedQuery = providedQuery.trim().lowercase()

                    if (normalizedQuery.isEmpty()) {
                        emit(CardSearchResult(emptyList(), emptyList()))
                        return@flow
                    }

                    val exactMatches = getExactSubstringMatches(
                        query = normalizedQuery,
                        candidates = normalizedCards!!
                    )

                    val approximateMatches = if (normalizedQuery.length >= MIN_QUERY_LENGTH_FOR_APPROXIMATE_MATCH) {
                        getApproximateSubstringMatches(
                            query = normalizedQuery,
                            candidates = normalizedCards!!,
                            existingMatches = exactMatches,
                            minSimilarityThreshold = minSimilarityThreshold
                        )
                    } else {
                        emptyList()
                    }

                    emit(CardSearchResult(exactMatches, approximateMatches))
                }
            }
            .flowOn(Dispatchers.Default)
    }

    /**
     * Performs a check if [SearchCardsUseCaseImpl] has a local copy of List<CardGalleryEntry>.
     * If it does not have it, fetches the data from [cardGalleryRepository] dependency.
     */
    private suspend fun ensureCardsCached() {
        if (allCards == null) {
            allCards = cardGalleryRepository.getAllCards().first()
            normalizedCards = allCards!!.map { it to it.name.lowercase() }
        }
    }

    /**
     * Finds exact string matches for a [query] within a of [candidates] cards.
     *
     * Iterates through all [candidates] cards and checks if the search [query] exists as
     * a contiguous block of text within the card's lowercase name. If it finds a match,
     * it remembers the original card and where in the name that match started (index).
     * Cards without an exact substring match are discarded.
     *
     * The results are then sorted, primarily by the starting position of the match (earlier is better),
     * and secondarily by the length of the card's name (shorter is better for ties).
     *
     * Finally it extracts only [CardGalleryEntry] objects from List<Pair<CardGalleryEntry, Int>>
     *
     * @param query The user's search input phrase. As per current implementation this would be
     * provided inside [invoke] via inner val normalizedQuery.
     * @param candidates A list of all cards provided in a Pair of [CardGalleryEntry] and
     * [CardGalleryEntry.name]. As per current implementation this would be facilitated by [normalizedCards]
     * @return A List of [CardGalleryEntry] objects
     */
    private fun getExactSubstringMatches(
        query: String,
        candidates: List<Pair<CardGalleryEntry, String>>
    ): List<CardGalleryEntry> {
        val matchesWithIndex: List<Pair<CardGalleryEntry, Int>> = candidates
            .mapNotNull { (card, name) ->
                name.indexOf(query)
                    .takeIf { it >= 0 }
                    ?.let { card to it }
            }

        val sortedMatches: List<Pair<CardGalleryEntry, Int>> = matchesWithIndex
            .sortedWith(compareBy(
                { it.second },
                { it.first.name.length }
            ))

        val finalResults: List<CardGalleryEntry> = sortedMatches
            .map { it.first }

        return finalResults
    }

    /**
     * Finds approximate string matches for a [query] within a list of [candidates] cards.
     *
     * Converts the input [candidates] to a [Sequence] and then excludes any cards that are
     * already present in the [existingMatches] list. The result is stored in `relevantCandidates`.
     *
     * Calculates a similarity score with [calculateTwoCharOverlapSimilarity] for each remaining card's name against the [query].
     * It then filters out cards whose similarity score falls below the [minSimilarityThreshold],
     * considering them irrelevant. The result is stored in `scoredAndFilteredCandidates`.
     *
     * Sorts the filtered candidates by their similarity score in descending order (highest similarity first).
     * Finally, it limits the number of results to the specified [maxResults] (or all if null).
     * The result is stored in `topRankedMatches`.
     *
     * Extracts only the [CardGalleryEntry] objects from the processed pairs and converts
     * the final [Sequence] back into a [List]. The result is stored in `finalApproximateResults`.
     *
     * @param query The user's search input phrase. As per current implementation this would be
     * provided inside [invoke] via inner val normalizedQuery.
     * @param candidates A list of all cards provided in a Pair of [CardGalleryEntry] and
     * [CardGalleryEntry.name]. As per current implementation this would be facilitated by [normalizedCards]
     * @param existingMatches A list of [CardGalleryEntry] objects that have already been
     * identified as exact matches. These cards are excluded to prevent duplicates.
     * @param minSimilarityThreshold The minimum similarity score (between 0.0 and 1.0) a card's name
     * must have against the query to be considered an approximate match.
     * @param maxResults The maximum number of approximate matches to return.
     * @return A [List] of [CardGalleryEntry] objects representing the top approximate matches (/all for [maxResults] = null)
     */
    private fun getApproximateSubstringMatches(
        query: String,
        candidates: List<Pair<CardGalleryEntry, String>>,
        existingMatches: List<CardGalleryEntry>,
        minSimilarityThreshold: Double,
        maxResults: Int? = null
    ): List<CardGalleryEntry> {
        val relevantCandidates: Sequence<Pair<CardGalleryEntry, String>> = candidates
            .asSequence()
            .filterNot { (card, _) -> existingMatches.contains(card) }

        val scoredAndFilteredCandidates: Sequence<Pair<CardGalleryEntry, Double>> = relevantCandidates
            .map { (card, name) -> card to calculateTwoCharOverlapSimilarity(query, name) }
            .filter { (_, similarity) -> similarity > minSimilarityThreshold }

        val topRankedMatches: Sequence<Pair<CardGalleryEntry, Double>> = scoredAndFilteredCandidates
            .sortedByDescending { (_, similarity) -> similarity }
            .take(maxResults ?: Int.MAX_VALUE)

        val finalApproximateResults = topRankedMatches
            .map { (card, _) -> card }
            .toList()

        return finalApproximateResults
    }

    /**
     * Calculates similarity for two-character blocks, bigrams, (Dice coefficient) between two strings.
     * This function measures the similarity by comparing the number of shared two-character sequences (bigrams)
     * between the two input strings. The result is a score between 0.0 and 1.0, where 1.0 indicates identical strings.
     *
     * If [s1] and [s2] are identical, returns 1.0 immediately
     * If either string has fewer than two characters, no bigrams can be formed, and the function returns 0.0.
     * Generates sets of all unique two-character overlapping substrings (bigrams) for both [s1] and [s2] using `windowed(2)`.
     * Finds the intersection of the two bigram sets (common bigrams).
     *
     * Used formula: `2 * (Number of Common Bigrams) / (Total Bigrams in s1 + Total Bigrams in s2)`.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return A [Double] representing the bigram similarity score between [s1] and [s2], ranging from 0.0 to 1.0.
     */
    private fun calculateTwoCharOverlapSimilarity(s1: String, s2: String): Double {
        if (s1 == s2) return 1.0
        if (s1.length < 2 || s2.length < 2) return 0.0

        val bigrams1 = s1.windowed(2).toSet()
        val bigrams2 = s2.windowed(2).toSet()
        val intersection = bigrams1 intersect bigrams2
        return 2.0 * intersection.size / (bigrams1.size + bigrams2.size)
    }
}