#!/bin/bash


# usage:
#   bash scripts/distribution.bash
#
# builds a source distribution archive and stores it in 'dist/'


function extract_from_manifest {
    grep -o $1'="[^"]*"' $SOURCE/AndroidManifest.xml | sed 's/'$1'="\(.*\)"/\1/'
}

function extract_from_static {
    grep -o $1'">[^<]*<' $SOURCE/res/values/static.xml | sed 's/'$1'">\(.*\)</\1/'
}



SOURCE=.
VERSION="`extract_from_manifest "versionName"`"
NAME="`extract_from_static "sname"`"
TARGET=$NAME"-"$VERSION".tar.gz"

mkdir dist

tar --transform s,^\.,"$NAME"_"$VERSION", -czvf dist/$TARGET $SOURCE         \
--exclude 'dist'                       \
--exclude 'bin'                        \
--exclude 'gen'                        \
--exclude 'libs'                       \
--exclude 'gen'                        \
--exclude 'local.properties'           \
--exclude 'proguard-project.txt'       \
--exclude 'assets'                     \
--exclude 'lint.xml'                   \
--exclude 'build.xml'                  \
--exclude 'proguard.cfg'               \
--exclude 'project.properties'         \
--exclude '.classpath'                 \
--exclude '.metadata'                  \
--exclude '.project'                   \
--exclude '.settings'
