# README

## Map Tiles
AAT does not automatically remove downloaded map tiles. This is to...

- reduce network traffic as much as possible.
- reduce battery drain.
- speed up the map view.

This means that you have to remove old tiles manually from time to time.
Go to `Settings->Map Tiles` to remove old map tiles.


## Offline Maps
### Download and store maps:
1. Download maps and render themes from one of these map providers:  
[OpenAndroMaps](http://www.openandromaps.org),[Freizeitkarte](http://www.freizeitkarte-osm.de/android/en/index.html) or [Mapsforge](http://download.mapsforge.org/)
2. Store extracted maps in the directory `maps` or in one of its subdirectories.  
Example: `maps/OAM/` contains `Alps.map`
3. Store extracted themes in a subdirectory of the directory containing the corresponding maps.  
Example: `maps/OAM/OAM/` contains `andromaps_cycle.xml`, `symbols/`, `patterns/` and other files.

### Enable offline maps
1. Inside AAT open the map settings dialog. (`Settings->Map Tiles`).
2. Select the directory that contains the map files (In this example `maps/OAM/` will be listed). 
3. Select a render theme.
4. Disable Mapnik and enable Mapsforge in `Menu->Map`.


## Where to find files?
AAT let's you create and view files in GPX format. This files are exchangeable with many other applications. They can also be edited in a text editor. 
These and other files are stored in subdirectories of the data directory. You can choose the location of the data directory from the settings.
The structure of the data directory is as follows: 

- `aat_data/activity[0-4]` - tracked activities
- `aat_data/log`           - activity that gets tracked at the moment
- `aat_data/nominatim`     - search result from OpenStreetMap Nominatim
- `aat_data/osm_features`  - OSM Features original file and parsed files
- `aat_data/overlay`       - overlays such as planned tracks or search results
- `aat_data/overpass`      - queries from the OSM Overpass server
- `aat_data/dem3`          - cached [Digital elevation data](http://viewfinderpanoramas.org/dem3.html) from the Shuttle Radar Topography Mission and other sources


## Overpass
### Overview
AAT lets you query the [Overpass API](http://wiki.openstreetmap.org/wiki/Overpass_API). Any result of a query can be saved to  `aat_data/overlay`. It then can be displayed as an overlay inside the map-view whenever needed.

### Example
`[amenity=restaurant]` will get you list of all restaurants located inside the area that was visible on the map-view before opening the Overpass dialog. 


## Calculating calories
AAT uses the formula `hours * MET * weight` to calculate the kcal used for a specific activity.
MET stands for [metabolic equivalent](https://en.wikipedia.org/wiki/Metabolic_equivalent). The higher the value, the more energy you would use
for this specific activity. A complete list of activities and MET values can be found [here](https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories).

Both MET and weight can be provided in the settings dialog. The MET value is leading the activity's name. For example: ` 6.8 bicycling, leisure, moderate effort`


## Issues and feedback
For questions, feedback and bugreports [report an issue](https://github.com/bailuk/AAT/issues) or send an e-mail to aat@bailu.ch

