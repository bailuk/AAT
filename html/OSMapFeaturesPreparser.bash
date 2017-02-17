#!/bin/bash

export map_features=Map_Features
export map_features_url=http://wiki.openstreetmap.org/wiki/$map_features

export parser=../simpleparser/build/libs/simpleparser.jar
export preparsed_dir=../app/src/main/assets/map_features

if [ -e "$map_features" ]; then
    echo "'$map_features' exists in '`pwd`' - skip downloading"
else
    wget $map_features_url
fi

if [ ! -e $map_features ]; then
    echo "download failed"
    exit 1
fi

if [ ! -d "$preparsed_dir" ]; then
    echo "'$preparsed_dir' does not exist"
    exit 1
fi

if [ -e "$parser" ]; then
    java -classpath $parser ch.bailu.simpleparser.MapFeaturesPreparser $map_features $preparsed_dir images
else 
    echo "'$parser' does not exist - run 'gradle jar' first"
    exit 1
fi


