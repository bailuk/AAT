#!/bin/bash

# Transform scripts/about.html from html to plain text.
# usage:
#   bash scripts/aboutAsText.bash

cat scripts/about.html \
| sed 's/&#0169;/(c)/'       \
| sed 's/<\/h2>/\n----------/'  \
| sed 's/<\/h1>/\n==========/'  \
| sed 's/<[^>]*>//g'
