#!/bin/bash

prefix="app"
source="${prefix}.xml"
target="${prefix}.gresource"
targetPath="../src/main/resources/${target}"

test -f "$source" || cd gresource

glib-compile-resources $source || exit 1
echo "Update '${targetPath}'"
cp -f "${target}" "${targetPath}"
