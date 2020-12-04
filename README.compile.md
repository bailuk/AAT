# Build
## Build-System
This project uses the [Gradle Build Tool](https://gradle.org/) for building.

## Dependencies
- Android SDK with or without [Android Studio](https://developer.android.com/studio/)
- [Android Debug Bridge (adb)](https://developer.android.com/studio/command-line/adb)

## Download
git clone https://github.com/bailuk/AAT.git

## Compile
```shell
export ANDROID_SDK_ROOT=~/Android/Sdk/
cd AAT
./gradlew build
find app/build/outputs -name "*.apk"
```

## Install
```shell
find app/build/outputs -name "*.apk"
adb install app/build/outputs/apk/debug/app-debug.apk
```
## Build with local MapsForge
Set path to MapsForge repository in `settings.gradle`
