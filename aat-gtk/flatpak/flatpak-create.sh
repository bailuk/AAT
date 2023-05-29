#!/bin/sh
test -d flatpak || cd ..
cd flatpak || exit 1

mkdir -p build || exit 1

export LANG=C # So we can google error messages

proxy_host="192.168.178.80"

if [ ! -z $proxy_host ]; then
  export http_proxy=http://${proxy_host}:8080
  export https_proxy=http://${proxy_host}:8080
fi

echo "_"
echo "Install dependencies"
flatpak install flathub org.gnome.Platform//44 org.gnome.Sdk//44 org.freedesktop.Sdk.Extension.openjdk17/x86_64/22.08 || exit 1

echo "_"
echo "Build flatpak"
flatpak-builder --force-clean build/build ch.bailu.aat.json || exit 1

echo "_"
echo "Install flatpak"
echo "flatpak-builder --user --install --force-clean build/build ch.bailu.aat.json || exit 1"

# Single-file bundle
# https://unix.stackexchange.com/questions/695934/how-do-i-build-a-flatpak-package-file-from-a-flatpak-manifest
# https://docs.flatpak.org/en/latest/single-file-bundles.html

echo "_"
echo "Export flatpak"
flatpak build-export build/export build/build || exit 1


echo "_"
echo "Build single-file bundle"
flatpak build-bundle build/export build/aat.flatpak "ch.bailu.aat"

echo "_"
echo "Install single-file bundle"
echo "> flatpak install build/aat.flatpak"

echo "_"
echo "Run flatpak"
echo "> flatpak run ch.bailu.aat"
