#!/bin/bash

set -e
# set -x

# This script generates Flatpak sources for a gradle project.

# It works by running the gradle build (--dry-run is usually enough) with
# a separate, new gradle user home (instead of ~/.gradle). All needed
# dependencies will be cached in this gradle user home. The script then moves
# all these cached artifacts into a directory structure of a maven repo.
# It also computes the hashes and checks the repos for where this artifact is
# available to generate the url.
# Finally, it writes the json file which can be used in the flatpak build
# manifest.

# You may want to modify these variables
# Where the sources json file should be generated:

# Original Script: https://github.com/TobTobXX/flatpak-builder-tools/tree/master/gradle

test -f gradlew || cd ..
test -f gradlew || cd ..
test -f gradlew || exit 1

# Must be an absolute path
project_root="$(pwd)/aat-gtk"
source_file="${project_root}/flatpak/gradle-sources.json"

# The target to generate the dependencies for:
gradle_target="aat-gtk:build"

# The maven repos:
REPO_BASEURL=(
	'https://repo1.maven.org/maven2/'
	'https://dl.google.com/android/maven2/'
	'https://plugins.gradle.org/m2/'
	'https://jitpack.io/'
)

gradle_user_home="${project_root}/build/flatpak/gradle"
maven_repo="${project_root}/build/flatpak/maven"

mkdir -p $gradle_user_home
mkdir -p $maven_repo

proxy_host="192.168.178.80"

if [ ! -z $proxy_host ]; then
  gradle_proxy="-Dhttps.proxyHost=${proxy_host} -Dhttps.proxyPort=8080 -Dhttp.proxyHost=${proxy_host} -Dhttp.proxyPort=8080"
  export http_proxy=http://${proxy_host}:8080
  export https_proxy=http://${proxy_host}:8080
fi

if [ -f "$source_file" ]; then
  mv $source_file "${source_file}-$(date -Iseconds)"
fi

echo "_"
echo "Downloading all dependencies into ${gradle_user_home}"
./gradlew -g "$gradle_user_home" "$gradle_target" $gradle_proxy --no-daemon -q

cd "$gradle_user_home/caches/modules-2/files-2.1" || exit 1

# Following two blocks are adapted from here:
# https://gist.github.com/danieldietrich/76e480f3fb903bdeaac5b1fb007ab5ac
# Thank you Daniel Dietrich!

# Transforms gradle cache paths to maven repo paths
function mavenize {
  IFS='/' read -r -a paths <<< "$1"
	groupId=$(echo "${paths[1]}" | tr . /)
	artifactId="${paths[2]}"
	version="${paths[3]}"
	echo "$groupId/$artifactId/$version"
}

echo "_"
echo "Copy every file from the cache to it's maven repo location"
find . -type f -print0 | while IFS= read -r -d '' file; do
	filename=$(basename "$file")
	source_dir=$(dirname "$file")
	target_dir="$maven_repo/$(mavenize "$file")"
	mkdir -p "$target_dir" && cp "$source_dir/$filename" "$target_dir/"
done

echo "_"
echo "All interesting files are now in the maven repo"
echo "Create the json sources file"
cd "$maven_repo"

echo '[' > "$source_file"

echo "_"
echo "Probe repository for each file and write source object to json"
find * -type f -print0 | while IFS= read -r -d '' file; do
	url=''
	for repo in "${REPO_BASEURL[@]}"; do
		url_to_try="${repo}${file}"
		if curl --HEAD "$url_to_try" --fail -L &> /dev/null; then
			url="$url_to_try"
			break
		fi
	done
	if [ -z "$url" ]; then
		echo "ERROR: No repo contains $file"
		exit 1
	fi
	hash="$(sha256sum "$file" | cut -f 1 -d ' ')"

	echo "_"
  echo "$url"
  echo "$hash"
  echo "$file"

	cat << HERE >> "$source_file"
	{
		"type": "file",
		"url": "$url",
		"sha256": "$hash",
		"dest": "maven-local/$(dirname "$file")",
		"dest-filename": "$(basename "$file")"
	},
HERE
done

# Remove last line in json file and replace with closing braces without comma
head -n -1 "$source_file" > temp.json && mv temp.json "$source_file"
echo '	}' >> "$source_file"
# And close the json array
echo ']' >> "$source_file"

cd "$projectRoot"

echo "_"
echo "Finished. Success is unknown until observed."
