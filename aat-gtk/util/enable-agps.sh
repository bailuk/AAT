#/bin/sh

#
# Enable and update agps on the PinePhone
# Updates agps assistance data and enables the modem and agps
#

## XtraPath (Qualcomm GPS assistance data)
# http://xtrapath1.izatcloud.net/xtra.bin
# http://xtrapath2.izatcloud.net/xtra3gr.bin

## ModemManager CLI
# mmcli -m any --location-enable-gps-nmea --location-enable-gps-raw
# mmcli -m any --location-get

assistance_data="xtra3gr.bin"

if [ "root" != $(whoami) ]; then
    echo "Script must be run as root"
    exit 1
fi

echo "Enable modem"
mmcli -m 0 -e || exit 1

if [ -f $assistance_data ]; then
    echo "$assistance_data already downloaded"
else
    echo "Download AGPS assistance data"
    curl -LO http://xtrapath2.izatcloud.net/$assistance_data || exit 1

    echo "Upload APGS assistance data to modem"
    mmcli -m 0 --location-inject-assistance-data=$assistance_data || exit 1
fi

echo "Enable AGPS"
mmcli -m 0 --location-enable-agps-msb || exit 1

echo "Show location status"
mmcli -m 0 --location-status || exit 1
