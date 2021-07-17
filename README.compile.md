# Overview
This project is written in Java and uses the [Gradle Build Tool](https://gradle.org/) for building.
Use the gradle wrapper to execute gradle:
`./gradlew task` Gives you a list of all build tasks.

# Download
git clone https://github.com/bailuk/AAT.git

# Modules
AAT is divided into the following modules:

## aat-lib
Platform independent library classes. Contains code that is shared by all plattform versions.

##  aat-android
Android version.

## aat-awt
AWT / [Swing](https://en.wikipedia.org/wiki/Swing_(Java)) / [freedesktop.org](https://www.freedesktop.org/wiki/) version.
This is an early alpha version. Currently it is only useable for testing

## Build Android version
### Prequisite
- Android SDK with or without [Android Studio](https://developer.android.com/studio/)
- [Android Debug Bridge (adb)](https://developer.android.com/studio/command-line/adb)

### Compile
```bash
export ANDROID_SDK_ROOT=~/Android/Sdk/
./gradlew :aat-android:build
find app/build/outputs -name "*.apk"
```

### Install
```bash
find app/build/outputs -name "*.apk"
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Build with local MapsForge
Set path to local MapsForge repository in `settings.gradle`

## compile, run and install AWT / Swing version
```bash
# build
./gradlew :aat-android:build

# build & run
./gradlew :aat-awt:run

# install
cd aat-awt/util
./install.sh
```
