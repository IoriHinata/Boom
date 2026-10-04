# Android build

Requirements: Android Studio, Android SDK 36, JDK 17.

Open the repository as a Gradle project and run the `app` configuration. APK command:

    ./gradlew :app:assembleDebug

The current APK shell is a real Android application. It deliberately does not claim that the structural solver is complete; the physics core remains behind an engine-independent backend boundary.
