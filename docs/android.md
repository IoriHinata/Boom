# Android target

The Android layer is deliberately thin: it owns lifecycle, UI, configuration and packaging. Structural state stays in the engine-independent simulation modules.

Debug APK is produced by GitHub Actions as `boom-debug-apk`. No signing keys or API keys belong in the repository.
