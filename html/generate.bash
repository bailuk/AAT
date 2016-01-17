#!/bin/bash
# Generates html and android resource files from markdown documentation.
# Depends on markdown: http://daringfireball.net

markdown --html4tags ../README.enduser.md > README.enduser.html
markdown --html4tags ../README.compile.md > README.compile.html
markdown --html4tags ../README.md > README.about.html


echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>" > ../res/values/generated.xml
echo "<resources>"                               >> ../res/values/generated.xml
bash htmlAsResource.bash README.about.html       >> ../res/values/generated.xml
bash htmlAsResource.bash README.enduser.html     >> ../res/values/generated.xml
echo "</resources>"                              >> ../res/values/generated.xml

