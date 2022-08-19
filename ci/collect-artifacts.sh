#!/bin/sh

mkdir -p dist/gtk || exit 1
cp aat-gtk/build/libs/* dist/gtk || exit 1
cp aat-gtk/util/install.sh dist/gtk || exit 1
cp aat-gtk/src/main/resources/svg/app-icon.svg dist/gtk || exit 1

mkdir -p dist/android || exit 1
cp -r aat-android/build/outputs/apk/* dist/android || exit 1
