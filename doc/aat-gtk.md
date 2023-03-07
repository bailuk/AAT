# AAT GTK

![screenshot](screenshot-gtk.png)


# Install

- Local: `util/install.sh`
- Remote (ssh): `util/install.sh user@host`
- Options: `--build` and `--run`


# GPS on a PinePhone

## Device

GNSS: GPS, GPS-A, GLONASS
Quectel Wireless Solutions Co., Ltd. EC25 LTE modem

## Resources

- [Mobian Wiki](https://wiki.mobian-project.org/doku.php?id=location)
- [Pinephone: GPS Ergebnis verbessern](https://marius.bloggt-in-braunschweig.de/2021/10/26/pinephone-gps-ergebnis-verbessern/)
- [geoclue2: how to get location and configure](https://unix.stackexchange.com/questions/479880/geoclue2-how-to-get-location-and-configure)
- [Proof of concept AGPS data loading for PinePhone ](https://gist.github.com/alastair-dm/263209b54d01209be28828e555fa6628)

## Script to enable GPS

`util/enable-apgs.sh`
- Enables Qualcomm Modem
- Downloads and updates AGPS assistance data
- Enables AGPS


## Disable suspend on PinePhone with mobian & phosh

Settings -> Power -> Automatic standby -> OFF for Battery
`util/power-management.sh`
