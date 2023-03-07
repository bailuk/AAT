# Overview

This project is written in Java and Kotlin and uses the [Gradle Build Tool](https://gradle.org/) for building.
Use the gradle wrapper to execute gradle:
`./gradlew task` Gives you a list of all build tasks.


# Download

git clone https://github.com/bailuk/AAT.git


# Modules

AAT is divided into the following modules:

## aat-lib

Platform independent library classes. Contains code that is shared by all platform versions.

##  aat-android

Android version.


## aat-gtk

AAT port to [java-gtk](https://github.com/bailuk/java-gtk). This is an early alpha version. 


## Android variant

### Prerequisite

- Android SDK with or without [Android Studio](https://developer.android.com/studio/)
- [Android Debug Bridge (adb)](https://developer.android.com/studio/command-line/adb)


### Build

```bash
export ANDROID_SDK_ROOT=~/Android/Sdk/ 
./gradlew aat-android:build
find app/build/outputs -name "*.apk"
```


### Install

```bash
find app/build/outputs -name "*.apk"
adb install app/build/outputs/apk/debug/app-debug.apk
```


### Build with local MapsForge

Set path to local MapsForge repository in `settings.gradle`

## Build, run and install GTK variant

```bash
# build
./gradlew aat-gtk:build

# build & run
./gradlew aat-gtk:run

# install
cd aat-gtk/util
./install.sh
```
