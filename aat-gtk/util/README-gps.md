# GPS on a PinePhone

## Device
GNSS: GPS, GPS-A, GLONASS
Quectel Wireless Solutions Co., Ltd. EC25 LTE modem


## Documentation
https://wiki.mobian-project.org/doku.php?id=location
https://marius.bloggt-in-braunschweig.de/2021/10/26/pinephone-gps-ergebnis-verbessern/
https://unix.stackexchange.com/questions/479880/geoclue2-how-to-get-location-and-configure

## XtraPath
http://xtrapath1.izatcloud.net/xtra.bin
http://xtrapath2.izatcloud.net/xtra3gr.bin


## ModemManager CLI
mmcli -m any --location-enable-gps-nmea --location-enable-gps-raw
mmcli -m any --location-get

mmcli -m any --location-inject-assistance-data=xtra3gr.bin
mmcli -m any --location-enable-agps-msb

mmcli -m any --location-status