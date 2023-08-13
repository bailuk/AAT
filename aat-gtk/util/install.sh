#!/bin/sh

help="Installs application for logged in user or for user on ssh host
Usage: ./install.sh [--build] [--run] [--help] [user@ssh_host]
Options:
  --build: build first
  --run: run after installation
  usar@ssh_host: install on a remote device"

# general
app="aat-gtk"
app_id="ch.bailu.aat"
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
  source_icon="${app_id}.svg"
else
  source_jar="aat-gtk/$build/$jar"
  source_icon="aat-gtk/gresource/icons/scalable/apps/${app_id}.svg"
  test -d gradle || cd ..
  test -d gradle || cd ..
fi

# destination
if [ -n "$remote" ]; then
  echo ">> install on '$remote'"
  home=$(ssh $remote echo '$HOME')
  xdg_data_home=$(ssh $remote echo '$XDG_DATA_HOME')
  cmd="ssh $remote"
  copy="scp"
  tor="$remote:"
else
  echo ">> install for '$(whoami)'"
  home=$HOME
  xdg_data_home=$XDG_DATA_HOME
  cmd="/bin/sh -c"
  copy="cp -v"
  tor=""
fi

# icon and desktop path
if [ "$xdg_data_home" = "" ]; then
  xdg_data_home="$home/.local/share"
fi
icon_path="${xdg_data_home}/icons/hicolor/scalable/apps"
desktop_path="${xdg_data_home}/applications/"

desktop="${desktop_path}/${app_id}.desktop"
icon="${icon_path}/${app_id}.svg"
data="${home}/.config/${app}"

# build
if [ "$option_build" = "--build" ]; then
    echo ">> build"
    ./gradlew aat-gtk:build || exit 1
fi

# install
if [ "$option_install" = "" ]; then
  $cmd mkdir -p ${icon_path}
  $cmd mkdir -p ${desktop_path}
  $cmd "test -d ${data} || mkdir ${data}" || exit 1
  $copy $source_jar "${tor}${data}/${app}.jar"  || exit 1
  $copy $source_icon "${tor}${icon}" || exit 1

  echo "create '${desktop}'"
  $copy aat-gtk/flatpak/ch.bailu.aat.desktop "${tor}${desktop}"
  $cmd sed -i "'s+Exec.*+Exec=java -jar ${data}/${app}.jar+'" ${desktop}

  $cmd "chmod 700 ${desktop}" || exit 1
fi

# run
if [ "$option_run" = "--run" ]; then
  java_cmd="java -jar ${data}/${app}.jar"
  echo ">> run '$java_cmd'"
  $cmd "$java_cmd" || exit 1
fi

exit 0
