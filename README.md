# ShieldChat

ShieldChat is a Secure Telegram Clone with an AI Smart Feed. It combines real-time messaging with end-to-end encryption principles and local AI-powered summaries to keep you updated on your conversations.

## Architecture Summary

*   **Client-Side Messaging**: Built with modern Android development practices using Kotlin and Jetpack Compose.
*   **Database**: Uses Firebase Realtime Database for instantaneous message delivery and syncing.
*   **AI Smart Feed**: Integrates with the Gemini API to automatically summarize unread messages and highlight important content directly in your feed.
*   **Local Persistence**: Utilizes Room Database for local caching and offline capabilities.
*   **Security Context**: E2EE concepts implemented (client-side) to ensure message privacy.

## Setup Instructions

Follow these steps to run the application locally:

### 1. Prerequisites

*   Android Studio (latest stable version recommended).
*   A Firebase project.
*   A Google AI Studio API key (for Gemini).

### 2. Firebase Configuration

1.  Go to the [Firebase Console](https://console.firebase.google.com/).
2.  Create a new project or select an existing one.
3.  Add an Android app to your Firebase project.
    *   Package Name: Check the `applicationId` in `app/build.gradle.kts` (e.g., `com.aistudio.shieldchat...`).
4.  Download the `google-services.json` file.
5.  Place the downloaded `google-services.json` file into the `app/` directory of this project.
6.  Enable Firebase Authentication (Phone number provider) and Realtime Database in your Firebase Console.

### 3. Environment Variables

1.  Rename the provided `.env.example` file to `.env`.
2.  Open `.env` and configure your API keys:
    ```env
    GEMINI_API_KEY=your_gemini_api_key_here
    ```

### 4. Build and Run

1.  Open the project in Android Studio.
2.  Allow Gradle to sync automatically.
3.  Select an emulator or a physical device.
4.  Click the **Run** button to compile and launch the application.

## Development

*   **UI Framework**: Jetpack Compose
*   **Language**: Kotlin
*   **Build Tool**: Gradle (Kotlin DSL)
