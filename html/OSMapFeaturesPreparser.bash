#!/bin/bash

export map_features=Map_Features
export map_features_url=http://wiki.openstreetmap.org/wiki/$map_features

export parser=../simpleio/build/libs/simpleio.jar
export parser_class=ch.bailu.simpleio.parser.MapFeaturesPreparser
export preparsed_dir=../app/src/main/assets/map_features


## Download Map_Features (html document) from osm server
if [ -e "$map_features" ]; then
    echo "'$map_features' exists in '`pwd`' - skip downloading"
else
    wget $map_features_url
fi

if [ ! -e $map_features ]; then
    echo "download failed"
    exit 1
fi

## Check if target directory for perparsed data exists
if [ ! -d "$preparsed_dir" ]; then
    echo "'$preparsed_dir' does not exist"
    exit 1
fi


## Preparse: extract relevant data from Map_Features and split it into several documents
if [ -e "$parser" ]; then
    java -classpath $parser $parser_class $map_features $preparsed_dir images
else 
    echo "'$parser' does not exist - run 'gradle jar' first"
    exit 1
fi


