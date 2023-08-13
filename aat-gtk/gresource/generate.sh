#!/bin/bash

prefix="app"
source="${prefix}.xml"
target="${prefix}.gresource"
targetPath="../src/main/resources/${target}"

glib-compile-resources $source || exit 1
echo "Update '${targetPath}'"
cp -f "${target}" "${targetPath}"
