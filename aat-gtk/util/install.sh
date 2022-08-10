#!/bin/sh

help="Installs application for logged in user or for user on ssh host
Usage: ./install.sh [--build] [--run] [--help] [user@ssh_host]
Options:
  --build: build first
  --run: run after installation
  usar@ssh_host: install on a remote device"
./aat-gtk/build/libs/aat-gtk-all.jar

# general
app="aat-gtk"
app_id="ch.bailu.${app}"
app_comment="AAT activity tracker and map viewer"
app_name="AAT"
jar="${app}-all.jar"
build="build/libs/"

# arguments
for i in "$@"; do
  if [ "$i" = "--help" ]; then
    echo "$help"
    exit 0
  elif [ "$i" = "--run" ]; then
    option_run="$i"
  elif [ "$i" = "--build" ]; then
    option_build="$i"
  elif [ "$i" = "--no-install" ]; then
    option_install="$1"
  else
    remote="$i"
  fi
done

# source
if [ -f $jar ]; then
  source_jar=$jar
  source_icon=app-icon.svg
else
  source_jar="tlg_gtk/$build/$jar"
  source_icon="tlg_gtk/src/main/resources/svg/app-icon.svg"
  test -d gradle || cd ..
  test -d gradle || cd ..
fi

# destination
if [ -n "$remote" ]; then
  echo ">> install on '$remote'"
  home=$(ssh $remote pwd)
  cmd="ssh $remote"
  copy="scp"
  tor="$remote:"
else
  echo ">> install for '$(whoami)'"
  home=$HOME
  cmd="/bin/sh -c"
  copy="cp -v"
  tor=""
fi

desktop="${home}/.local/share/applications/${app_id}.desktop"
data="${home}/.config/${app}"

# build
if [ "$option_build" = "--build" ]; then
    echo ">> build"
    ./gradlew build || exit 1
fi

# install
if [ "$option_install" = "" ]; then
  $cmd "test -d ${data} || mkdir ${data}" || exit 1
  $copy $source_jar "${tor}${data}/${app}.jar"  || exit 1
  $copy $source_icon "${tor}${data}/${app}.svg" || exit 1

  echo "create '${desktop}'"
  $cmd "cat > ${desktop}" << EOF
[Desktop Entry]
Type=Application
Terminal=false
Exec=java -jar ${data}/${app}.jar
Name=${app_name}
Comment=${app_comment}
Icon=${data}/${app}.svg
EOF

  $cmd "chmod 700 ${desktop}" || exit 1
fi

# run
if [ "$option_run" = "--run" ]; then
  java_cmd="java -jar ${data}/${app}.jar"
  echo ">> run '$java_cmd'"
  $cmd "$java_cmd" || exit 1
fi

exit 0
