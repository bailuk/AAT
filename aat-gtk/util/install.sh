#!/bin/sh
#
# Compiles AAT and installs it for the current user
# Usage: ./install.sh
#

data="${HOME}/aat_data"
desktop="${HOME}/.local/share/applications/ch.bailu.aat_awt.desktop"

test -d "${data}" || mkdir "${data}"

cd ../../ || exit 1
./gradlew aat-gtk::build || exit 1

ssh $remote "test -d ${data} || mkdir ${data}" || exit 1
cp  aat-gtk/build/libs/aat-gtk-all.jar"${data}/aat.jar" || exit 1
cp  src/main/resources/images/icon.svg "${data}/aat.svg" || exit 1

cat > "${desktop}" << EOF
[Desktop Entry]
Version=1.0
Type=Application
Terminal=false
Exec=java -jar ${data}/aat.jar
Name=AAT
Comment=AAT activity tracker and map viewer
Icon=${data}/aat.svg
EOF

chmod 700 "${desktop}"
