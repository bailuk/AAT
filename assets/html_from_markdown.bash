#!/bin/bash
# Generates html files from markdown documentation.
# Depends on markdown: http://daringfireball.net
#
# ToDo: 
#    - htmlize some characters: ° to &deg;  (c) to &#169;
#    - remove screenshot sections
#

RES="../app/src/main/assets/documentation"
markdown --html4tags ../README.enduser.md > $RES/README.enduser.html
markdown --html4tags ../README.md > $RES/README.about.html




