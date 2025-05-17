package luke.koz.gwentapi.ui.model.carddetails

//todo: Card displayed in gallery should use this too. refactor Async there to use CardImageOverlayModel
sealed class CardImageOverlayModel {
    abstract val basePath: String
    abstract val imageName: String
    open val dynamicValue: String? = null

    fun buildImageUrl(): String {
        val dynamicPart = dynamicValue?.let { "_$it" } ?: ""
        return "https://gwent.one/image/gwent/assets/$basePath/${imageName}${dynamicPart}.png"
    }

    sealed class PowerImageOverlay : CardImageOverlayModel() {
        override val basePath: String = "card/number/low"
        override val imageName: String = "power"

        data class WithValue(val value: String) : PowerImageOverlay() {
            override val dynamicValue: String = value
        }
    }

    sealed class ProvisionImageOverlay : CardImageOverlayModel() {
        object Icon : ProvisionImageOverlay() {
            override val basePath: String = "card/banner/low"
            override val imageName: String = "provision_icon"
        }

        data class Number(val value: String) : ProvisionImageOverlay() {
            override val basePath: String = "card/number/low"
            override val imageName: String = "provision"
            override val dynamicValue: String = value
        }
    }

    sealed class RarityImageOverlay : CardImageOverlayModel() {
        override val basePath: String = "card/other/medium"
        override val imageName: String = "rarity"

        data class WithValue(val value: String) : RarityImageOverlay() {
            override val dynamicValue: String = value
        }
    }

    sealed class BannerTopImageOverlay : CardImageOverlayModel(){
        object Icon : BannerTopImageOverlay() {
            override val basePath: String = "card/banner/low"
            override val imageName: String = "default_neutral"
        }
    }

    sealed class BannerBottomImageOverlay : CardImageOverlayModel(){
        object Icon : BannerBottomImageOverlay() {
            override val basePath: String = "card/banner/low"
            override val imageName: String = "provision_neutral"
        }
    }
}