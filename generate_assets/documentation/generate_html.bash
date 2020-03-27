#!/bin/bash
# Generate documentation that is used from within the app by
# generating html files from markdown documentation.
# Depends on 'markdown': http://daringfireball.net
#


# Use sed to htmlize some characters:
SED1="sed s/(c)/\&\#169\;/g"        # (c) ->  &#169;
SED2="sed s/\°/\&deg\;/g"           # °   ->  &deg;
SED3="sed s/\.\.\./\&\#8230\;/g"    # ... ->  &#8230;


# Generate html with htmlized characters:
function to_html { 
     markdown --html4tags | $SED1 | $SED2 | $SED3
}


# Extract sections (from README.md):
function extract {
    # Print lines starting from "# AAT"
    # Delete lines starting from "## Screen"
    sed -n '/## Availability/,$p' | sed '/## Screen/,$d'
}

# Projects root directory (relative)
ROOT="../.."

# Assets directory where documentation gets stored:
RES="$ROOT/app/src/main/assets/documentation"


# Generate documentation that is used from within the app:
cat $ROOT/README.enduser.md | to_html > $RES/README.enduser.html
cat $ROOT/README.md | extract | to_html > $RES/README.about.html

echo "Generated $RES/README.enduser.html"
echo "Generated $RES/README.about.html"
