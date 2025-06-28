# Gwent Card Gallery App

This application offers a comprehensive card gallery for the popular game "Gwent: The Witcher Card Game". Users can browse the full collection, store it locally, inspect individual card statistics, appreciate the artworks and search for specific cards. The app allows users to mark cards as favorites and observe how many others agree with their assessment.

## Features

- **Card Gallery**: Explore a list of Gwent cards, with their art, name, flavour text and number of likes each card got from other users. 
- **Offline Access**: Store fetched card data (including statistics and graphic files) locally for browsing offline and preserving bandwidth.
- **Detailed Card View**: Tap on any card to view a detailed breakdown of its statistics and abilities.
- **Like/Unlike Functionality**: Mark your favorite cards, with preferences synchronized across devices and visible to other users.
- **Search Functionality**: Find specific cards within the gallery by their name.

## Architecture

The project follows a modular architecture, with emphasis on separation of concerns. This design was implemented to facilitate testability, simulate collaborative workflow and reduced build times.

Key architectural layers and their corresponding modules:
- `:app` - The application's entry point, primarily responsible for application-level setup and navigation handling
- `:data` - Handles all communication with data sources (Firebase, API, local storage), including network requests and local persistence
- `:domain` - Contains the core business logic and use cases, acting as an intermediary between the data and presentation layers
- `:feature` - Contains user-facing UI elements and their associated logic for app features. Each feature is given separate `:feature:name` module
- `:core` - A collection of shared functionalities, reusable components, and common definitions used across different modules

## Stack

- **Kotlin programming language** - The core programming language
- **Jetpack Compose** - Modern Android UI framework
- **Kotlin Coroutines & Flow** - Modern management of asynchronous code and observing dynamic data
- **Manual dependency injection** - Chosen over external frameworks (e.g., Dagger-Hilt, Koin) to simplify the project setup given its limited scope
- **Retrofit** - As the intermediary between app and API
- **Coil** - Image loading library for efficient image retrieval and caching
- **Room** - Provides the means of storage for fetched API data 
- **Firebase** - Used for user authorization and subsequent likes/unlikes submissions

## Getting Started

To get a local copy running on your machine:
1. **Clone the repository**
```git clone https://github.com/LukeKozielecki/GwentApi/```
2. **Open in Android Studio**
3. **Build and Run**
Select emulator or physical device sufficient for minimal Gradle requirements.

## License

MIT License

Copyright (c) 2025 Lucjan Kozielecki

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.