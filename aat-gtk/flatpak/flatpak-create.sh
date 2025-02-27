#!/usr/bin/env bash

set -e

short_id="aat"
full_id="ch.bailu.$short_id"
build_path="build/flatpak"

# As described in https://docs.flathub.org/docs/for-app-authors/submission/

flatpak_base="build/flatpak"
flatpak_export="${flatpak_base}/export"
flatpak_repo="${flatpak_base}/repo"
flatpak_build="${flatpak_base}/build"

mkdir -p ${flatpak_repo}
mkdir -p ${flatpak_build}
mkdir -p ${flatpak_export}

export LANG=C # So we can google error messages

if [ ! -z $1 ]; then
  echo "_"
  echo "Using proxy: $1"
  export http_proxy=http://${1}:8080
  export https_proxy=http://${1}:8080
fi

echo "_"
echo "Add flathub repo for user"
flatpak remote-add --user --if-not-exists flathub https://dl.flathub.org/repo/flathub.flatpakrepo

echo "_"
echo "Install flatpak builder"
flatpak install --user flathub org.flatpak.Builder || exit 1

echo "_"
echo "Build and install flatpak"
flatpak run org.flatpak.Builder \
  --force-clean \
  --sandbox \
  --user \
  --install \
  --install-deps-from=flathub \
  --ccache \
  --mirror-screenshots-url=https://dl.flathub.org/media/ \
  --repo=${flatpak_repo} \
  ${flatpak_build} "$full_id.json"

# Single-file bundle
# https://unix.stackexchange.com/questions/695934/how-do-i-build-a-flatpak-package-file-from-a-flatpak-manifest
# https://docs.flatpak.org/en/latest/single-file-bundles.html

echo "_"
echo "Lint manifest"
flatpak run --command=flatpak-builder-lint org.flatpak.Builder manifest ch.bailu.aat.json || true

echo "_"
echo "Lint repo"
flatpak run --command=flatpak-builder-lint org.flatpak.Builder repo ${flatpak_repo} || true

echo "_"
echo "Build single-file bundle"
echo "> flatpak build-bundle ${flatpak_export} ${flatpak_base}/${short_id}.flatpak "$full_id""

echo "_"
echo "Export flatpak"
echo "> flatpak build-export ${flatpak_export} ${flatpak_build}"

echo "_"
echo "Install system-wide"
echo "> flatpak install --system ./${flatpak_export}  ${full_id}"

echo "_"
echo "Run flatpak"
echo "> flatpak run $full_id"
