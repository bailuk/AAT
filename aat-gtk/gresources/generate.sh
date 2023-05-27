#!/bin/bash

prefix="app"
source="${prefix}.xml"
target="${prefix}.gresource"


glib-compile-resources $source || exit 1
cp -f "${target}" "../src/main/resources/${target}"
