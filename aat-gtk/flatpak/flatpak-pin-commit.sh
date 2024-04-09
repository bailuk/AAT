#!/usr/bin/env bash

out_file="ch.bailu.aat.json"
in_file="${out_appstream}.in"
changelogs="../../metadata/en-US/changelogs"
git_hash=$(git show --format="%H" --no-patch)

echo "_"
echo "update '${out_file}' (commit: $git_hash)"
sed -ie 's/"branch": .*"/"branch": '\"${git_hash}\"'/g' "${out_file}"
