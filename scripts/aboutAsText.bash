#!/bin/bash

# Transform scripts/about.html from html to plain text.
# usage:
#   bash scripts/aboutAsText.bash

cat scripts/about.html \
| sed 's/&#0169;/(c)/'       \
| sed 's/<\/h2>/\n----------/'  \
| sed 's/<h2>//' \
| sed 's/<\/h1>/\n==========/'  \
| sed 's/<h1>//' \
| sed 's/<p>//'  \
| sed 's/<\/p>//'\
| sed 's/<b>/</'  \
| sed 's/<\/b>/>/'\
| sed 's/<br>//'  \
| sed 's/<!DOCTYPE html>//'\
| sed 's/<html>//'\
| sed 's/<body>//'\
| sed 's/<\/html>//'\
| sed 's/<\/body>//'\
