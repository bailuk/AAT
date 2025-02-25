#!/bin/sh

if [ -z "$1" ]; then
	echo "usage $1 path-to-umlet-installation"
	exit 1
fi

files=./*.uxf

for file in $files; do
	java -jar $1/umlet.jar -action=convert -format=svg -filename=$file
done

