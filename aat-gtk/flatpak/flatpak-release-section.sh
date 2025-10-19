#!/bin/bash

description=$(grep \^v ${1})
date=$(git log -1 --format=%ad --date=format:%Y-%m-%d -- ${1})
version=$(echo ${description} | grep -oP '(?<=^v)\d+(\.\d+)*' ${1})

if [ -z "$date" ]; then
  date=$(date -I)
fi

# Start release section
echo "        <release version=\"$version\" date=\"$date\">"

# Add URL for release details
echo "            <url type=\"details\">https://github.com/bailuk/AAT/releases/tag/v${version}</url>"

# Start description section
echo "            <description>"
echo "                <p>${description}</p>"
echo "                <ul>"

# Parse and add list of changes
while IFS= read -r change; do
    # Skip lines that don't contain changes
    if [[ $change == -* ]]; then
        change=$(echo "$change" | sed 's/^- //')
        echo "                    <li>$change</li>"
    fi
done < "$1"

# End description and release sections
echo "                </ul>"
echo "            </description>"
echo "        </release>"
