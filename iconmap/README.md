# Overpass

## Overview
AAT lets you query the [Overpass API](http://wiki.openstreetmap.org/wiki/Overpass_API). Any result of a query can be saved to  `aat_data/overlays`. It then can be displayed as an overlay inside the map-view whenever needed.

## Example
`[amenity=restaurant]' will get you list of all restaurants located inside the area that was visible on the map-view before opening the Overpass dialog. 

## Map Icons
Instead of the default symbol you can use the [map icons from SJJB Management](http://www.sjjb.co.uk/mapicons/):
- Download the [PNG Icons Set](http://www.sjjb.co.uk/mapicons/downloads/)
- Extract the icon set to `aat_data/overpass/icons/`.
- Download [iconmap.txt from GitHUB](https://raw.githubusercontent.com/bailuk/AAT/master/iconmap/iconmap.txt)
- Copy `iconmap.txt` to `aat_data/overpass/icons/`.
