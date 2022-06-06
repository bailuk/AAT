#!/bin/sh
#
# Compiles AAT and installs it for the current user
# Usage: ./install.sh
#

desktop_dir="${HOME}/.local/share/applications"
desktop="${desktop_dir}/ch.bailu.aat_gtk.desktop"
data="${HOME}/aat_data"

cd ../../ || exit 1
./gradlew aat-gtk::build || exit 1

test -d "${data}" || mkdir "${data}"
cp  aat-gtk/build/libs/aat-gtk-all.jar "${data}/aat.jar" || exit 1
cp  aat-gtk/src/main/resources/images/icon.svg "${data}/aat.svg" || exit 1

mkdir -p ${desktop_dir} || exit 1
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
