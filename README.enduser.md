# README

## Getting started with the UI

### Main view
When starting AAT the first time it shows the **main view**. That's basically a list of sub-views you can open. The first 3 entries are **"Copckpit A, Cockpit B, and Map"** if you have one of these open you can cycle through them with the down arrow. The right arrow switches trough the different pages of the view you are in.

### Map view (or full screen map)
In the **map view** the down arrow is in the main bar (tap the top area of the map). For zooming and navigating tap the bottom area of the map. For grid, search, overpass etc. tap  the right area of the map. For the draft editor tap the left side of the map. 
The track that gets currently recorded is always displayed on the map.

### Display recorded track
Now once you have stopped the tracker go back to the **main view** with your phones back button. Then select **Track list** to show a list of all recorded GPX tracks. Every track has a preview image if you tap on this image you'll get a context menu for this GPX-file. _Here you can for example select this track for one of the 4 overlay slots._ You can display any recorded track by taping the text area of the list entry. (Always go back to the list with the phones back button). In the track view there are again different pages (graph, summary and map) and you can cycle trough all tracks with the up and down arrow.

There are different pages in the **Track list** view (Access them with < ... > ). This pages offer **a map as an alternative to the track list**, **summary information** and **filter options**.


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
AAT uses the formula `time[hours] * MET * weight[kg]` to calculate the kcal used for a specific activity.
MET stands for [metabolic equivalent](https://en.wikipedia.org/wiki/Metabolic_equivalent). The higher the value, the more energy you would use
for this specific activity. A complete list of activities and MET values can be found [here](https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories).

Both MET and weight can be provided in the settings dialog. The MET value is leading the activity's name. For example: ` 6.8 bicycling, leisure, moderate effort`


## Contribution
### Issues and feedback
For questions, feedback and bugreports [report an issue](https://github.com/bailuk/AAT/issues) or send an e-mail to aat@bailu.ch

### Translation
You can open a pull request or send me your translation as e-mail or publish them as an issue report.

#### File tree structure
Resources (like text and icons) are located in [app/src/main/res](https://github.com/bailuk/AAT/tree/master/app/src/main/res). 

English text strings are in this file: [/app/src/main/res/values/strings.xml](https://github.com/bailuk/AAT/blob/master/app/src/main/res/values/strings.xml)

German translations are in this file: [/app/src/main/res/values-de/strings.xml](https://github.com/bailuk/AAT/blob/master/app/src/main/res/values-de/strings.xml)

#### Modify
- To add a new language just add a `value-<language code>` directory containing a `strings.xml` file.
- Use the English (original) strings.xml as a template.
- Use the same structure (order) as in the original file
- You don't have to translate everything. Just remove an entry and English is used as a fall back.
- You can modify existing strings.
- You can add new suggestions for strings.
- If a ° gets displayed somewhere inside the app it means the string is not yet in the resource file (only in java code). See [app/src/main/java/ch/bailu/aat/util/ToDo.java](https://github.com/bailuk/AAT/blob/master/app/src/main/java/ch/bailu/aat/util/ToDo.java)
