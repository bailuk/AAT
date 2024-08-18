#!/bin/sh

short_id="aat"
full_id="ch.bailu.$short_id"

test -d flatpak || cd ..
cd flatpak || exit 1

mkdir -p build || exit 1

export LANG=C # So we can google error messages

if [ ! -z $1 ]; then
  echo "_"
  echo "Using proxy: $1"
  export http_proxy=http://${1}:8080
  export https_proxy=http://${1}:8080
fi

echo "_"
echo "Install dependencies"
flatpak install flathub org.gnome.Platform//44 org.gnome.Sdk//44 org.freedesktop.Sdk.Extension.openjdk17/x86_64/22.08 || exit 1

echo "_"
echo "Build flatpak"
flatpak-builder --force-clean build/build "$full_id.json" || exit 1

echo "_"
echo "Install flatpak"
echo "flatpak-builder --user --install --force-clean build/build $full_id.json || exit 1"

# Single-file bundle
# https://unix.stackexchange.com/questions/695934/how-do-i-build-a-flatpak-package-file-from-a-flatpak-manifest
# https://docs.flatpak.org/en/latest/single-file-bundles.html

echo "_"
echo "Export flatpak"
flatpak build-export build/export build/build || exit 1


echo "_"
echo "Build single-file bundle"
flatpak build-bundle build/export build/aat.flatpak "$full_id"

echo "_"
echo "Install single-file bundle"
echo "> flatpak install build/$short_id.flatpak"

echo "_"
echo "Run flatpak"
echo "> flatpak run $full_id"
