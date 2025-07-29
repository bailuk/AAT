#!/bin/sh

mkdir -p dist/gtk/aat-gtk
cp aat-gtk/build/libs/* dist/gtk/aat-gtk
cp aat-gtk/util/install.sh dist/gtk/aat-gtk
cp aat-gtk/gresource/icons/scalable/apps/ch.bailu.aat.svg dist/gtk/aat-gtk

mkdir -p dist/android/aat-android
cp -r aat-android/build/outputs/apk/* dist/android/aat-android

mkdir -p dist/reports/aat-lib
mkdir -p dist/reports/aat-android
mkdir -p dist/reports/build
cp -r build/reports/* dist/build
cp -r aat-android/build/reports/* dist/reports/aat-android
cp -r aat-lib/build/reports/* dist/reports/aat-lib
