#!/usr/bin/env bash

out_file="ch.bailu.aat.metainfo.xml"
in_file="${out_file}.in"
changelogs="../../metadata/en-US/changelogs"
app_version=$(grep appVersionName ../../gradle.properties | cut -d "=" -f 2)

echo "_"
echo "generate '${out_file}' (version: $app_version)"

cat $in_file > $out_file
echo "    <releases>"  >> $out_file

# List all changelog-files and sort them
sorted_files=$(ls ${changelogs} | sort -r)

# Iterate through the sorted list of changelog-files
for file in $sorted_files; do
    if [ $file == "36.txt" ]; then
        break
    fi
    ./flatpak-release-section.sh ${changelogs}/${file} >> $out_file
done

echo "    </releases>" >> $out_file
echo "</component>"    >> $out_file
