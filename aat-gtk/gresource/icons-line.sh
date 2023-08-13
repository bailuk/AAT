#!/bin/sh

iconName=$(echo -n "$1" | sed s/\.svg//g)
constName=$(echo -n $iconName | sed s/\.svg//g | sed -r 's/_([a-z])/\U\1/g' | sed -r 's/-([a-z])/\U\1/g' | sed -r 's/\.([a-z])/\U\1/g' | sed -r 's/^([A-Z])/\l\1/')

echo "val $constName = Str(\"$iconName\")"
