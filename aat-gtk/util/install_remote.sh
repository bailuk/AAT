#!/bin/sh
#
# Compile AAT and install and run it on a remote host via ssh for a specific user
# Example usage: ./install_remote.sh user@mobian
#

if [ $# -eq 0 ]
  then
    echo "Example usage: ./install_remote.sh user@mobian"
    exit
fi


remote=$1

echo "Install to ${remote}"

home=$(ssh $remote pwd)
desktop="${home}/.local/share/applications/ch.bailu.aat_awt.desktop"
data="${home}/aat_data"

cd ../../ || exit 1
./gradlew aat-gtk::fatJar || exit 1

ssh $remote "test -d ${data} || mkdir ${data}" || exit 1
scp  aat-gtk/build/libs/aat-gtk.jar "${remote}:${data}/aat.jar"  || exit 1
scp  aat-gtk/src/main/resources/images/icon.svg "${remote}:${data}/aat.svg" || exit 1

ssh "${remote}" "cat > ${desktop}" << EOF
[Desktop Entry]
Version=1.0
Type=Application
Terminal=false
Exec=java -jar ${data}/aat.jar
Name=AAT
Comment=AAT activity tracker and map viewer
Icon=${data}/aat.svg
EOF

ssh $remote chmod 700 "${desktop}" || exit 1
ssh -X $remote java -jar "${data}/aat.jar" || exit 1

exit 0

