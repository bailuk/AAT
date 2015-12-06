#!/bin/bash

# Transform scripts/about.html from html to an android resource string.
# To replace the 'about'-resource in 'res/values/strings.xml'
# usage:
#   bash scripts/aboutAsResource.bash

cat scripts/about.html \
| sed 's/</\&lt;/g'       \
| sed 's/&lt;!DOCTYPE html>//'\
| sed 's/&lt;html>/<string name="about">/'\
| sed 's/&lt;body>//'     \
| sed 's/&lt;\/body>//'   \
| sed 's/&lt;\/html>/<\/string>/'

