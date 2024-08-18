#!/usr/bin/env bash

out_file="ch.bailu.aat.json"
changelogs="../../metadata/en-US/changelogs"
git_hash=$(git show --format="%H" --no-patch)

echo "_"
echo "update '${out_file}' (commit: $git_hash)"
sed -i 's/"commit": .*"/"commit": '\"${git_hash}\"'/g' "${out_file}"
