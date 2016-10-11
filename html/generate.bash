#!/bin/bash
# Generates html and android resource files from markdown documentation.
# Depends on markdown: http://daringfireball.net

RES="../app/src/main/res"
markdown --html4tags ../README.enduser.md > README.enduser.html
markdown --html4tags ../README.md > README.about.html



echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>" > $RES/values/generated.xml
echo "<resources>"                               >> $RES/values/generated.xml
bash htmlAsResource.bash README.about.html       >> $RES/values/generated.xml
bash htmlAsResource.bash README.enduser.html     >> $RES/values/generated.xml
echo "</resources>"                              >> $RES/values/generated.xml

