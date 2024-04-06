#!/usr/bin/env bash
# Generate documentation that is used from within the app by
# generating html files from markdown documentation.
# Depends on 'markdown': https://daringfireball.net/projects/markdown/

manual="manual"

# Use sed to htmlize some characters:
sed1="sed s/(c)/\&\#169\;/g"        # (c) ->  &#169;
sed2="sed s/\°/\&deg\;/g"           # °   ->  &deg;
sed3="sed s/\.\.\./\&\#8230\;/g"    # ... ->  &#8230;

# Generate html with htmlized characters:
function to_html {
     markdown --html4tags | $sed1 | $sed2 | $sed3
}

# Projects root directory (relative)
root="../../.."

# Assets directory where documentation gets stored:
res="$root/aat-android/src/main/assets/documentation"

# Generate documentation that is used from within the app:
cat $root/doc/${manual}.md | to_html > $res/${manual}.html

echo "Generated $res/${manual}.html"
