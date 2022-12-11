#!/bin/sh


if [ "$1" = "inactive" ] ||  [ "$1" = "suspend" ]; then
    echo _
    echo "Set to power management to ${1}"
    dconf write /org/gnome/settings-daemon/plugins/power/power-button-action \'$1\'
    dconf write /org/gnome/settings-daemon/plugins/power/sleep-inactive-battery-type \'$1\'
else
    echo _
    echo "No valid option provided. Possible options are: 'inactive' or 'suspend'"
fi

echo _
echo "/org/gnome/settings-daemon/plugins/power/power-button-action"
dconf read /org/gnome/settings-daemon/plugins/power/power-button-action

echo _
echo "/org/gnome/settings-daemon/plugins/power/sleep-inactive-battery-type"
dconf read /org/gnome/settings-daemon/plugins/power/sleep-inactive-battery-type
