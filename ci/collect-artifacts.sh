#!/bin/sh

mkdir -p dist/gtk/aat-gtk || exit 1
cp aat-gtk/build/libs/* dist/gtk/aat-gtk || exit 1
cp aat-gtk/util/install.sh dist/gtk/aat-gtk || exit 1
cp aat-gtk/gresource/icons/scalable/apps/ch.bailu.aat.svg dist/gtk/aat-gtk || exit 1

mkdir -p dist/android/aat-android || exit 1
cp -r aat-android/build/outputs/apk/* dist/android/aat-android || exit 1

mkdir -p dist/reports/aat-lib || exit 1
mkdir -p dist/reports/aat-android || exit 1
mkdir -p dist/reports/build || exit 1
cp -r build/reports/* dist/build
cp -r aat-android/build/reports/* dist/reports/aat-android
cp -r aat-lib/build/reports/* dist/reports/aat-lib
