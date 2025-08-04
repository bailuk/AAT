# Getting started with the UI

## Main view

When starting **AAT-Android** the first time it shows the **main view** with a list of sub-views you can open. 

The first 3 entries are **"Cockpit A, Cockpit B, and Map"**. 

Use the right arrow to switch trough the different pages of the view.

## Map view (and full screen map)

**Tap the edges of the map view, to get extra functionality**:

- Top edge: Cycle trough cockpit views.
- Left edge: Edit current track or `draft.gpx`.
- Right edge: Configure map, select grid, map search, import/export of center location.
- Bottom edge: Zoom in and out, lock to current position, frame tracks.

## Display recorded track

Once you have stopped the tracker go back to the **main view** with your phones back button. 
Then select **Track list** to show a list of all recorded GPX tracks. 

Every track has a preview image if you tap on this image, you'll get a context menu for this GPX-file.
_Here you can for example select this track for one of the 4 overlay slots._

You can display any recorded track by taping the text area of the list entry.
(Always go back to the list with the phones back button). 

In the track view there are again different pages (graph, summary and map) and you can cycle trough all tracks with the up and down arrow.

There are different pages in the **Track list** view (Access them with < ... > ). These pages offer **a map as an alternative to the track list**, **summary information** and **filter options**.

# Map Tiles

AAT does not automatically remove downloaded and cached map tiles. Go to `Preferences->Map` and scroll down to `Trim tile cache` to remove old map tiles manually.

# Install Offline Maps

## General

- Download maps, themes and POI-databases from one of the supported [map providers](https://github.com/mapsforge/mapsforge/blob/master/docs/Mapsforge-Maps.md).
- Extract zip-archives.
- Save those files to `/maps` on the sd-card or internal storage.

## OpenAndroMaps step by step

[OpenAndroMaps](http://www.openandromaps.org) offers global coverage, POI-databases and a nice outdoor theme.

1. Select a map from [Countries and Regions](https://www.openandromaps.org/en/downloads/countrys-and-regions). Download the default option: V5 Map and Poi-Database as a zip-archive.
2. Extract this zip-archive to `/maps` or to one of its subdirectories.
3. Navigate to [Elevate Map Style](https://www.openandromaps.org/en/legend/elevate-mountain-hike-theme) and download `Elevate.zip` from `Manual download (advanced)`.
4. Extract `Elevate.zip` to `/maps/elevate`.
5. Inside AAT open the map preferences dialog. (`Preferences->Map`).
6. Tap `Offline map`. This will list all maps stored in `/maps`.
7. Tap `Offline map theme` and select `Elevate.xml`.
8. Disable `Mapnik` and enable `Offline Elevate` in `Menu->Map`.

# Where to find files?

AAT lets you create and view files in GPX format. These files are exchangeable with many other applications.
They can also be edited in a text editor. 
These and other files are stored in subdirectories of the data directory. 

Storing data in `/aat_data` is preferred by AAT. This needs special permission but makes it easier to access and share files.

With limited permission **AAT-Android** will store files in `/Android/data/ch.bailu.aat/` which is private space for the application and a standard on Android. This is also managed somehow by Android _which means that files there are getting deleted when uninstalling AAT_.

On **AAT-GTK** the location of the data directory is either `~/aat_data` or `~/.config/aat-gtk`.

You can choose any location for the data directory in the preferences: `Preferences->General/System/Data directory`.

The structure (subdirectories) of the data directory is as follows: 

- `aat_data/activity[0-4]` : tracked activities
- `aat_data/log`           : activity that gets tracked at the moment
- `aat_data/query`         : search results from OSM-Nominatim, OSM-Overpass and offline POI 
- `aat_data/overlay`       : GPX-files that can be used as overlays: Routes, way-points, search results...
- `aat_data/dem3`          : cached [Digital elevation data files ](https://bailu.ch/dem3/)
- `aat_data/maps`          : alternative location for offline maps

# Calculating calories

AAT uses the formula `time[hours] * MET * weight[kg]` to calculate the kcal used for a specific activity.
MET stands for [metabolic equivalent](https://en.wikipedia.org/wiki/Metabolic_equivalent). The higher the value, the more energy you would use
for this specific activity. A complete list of activities and MET values can be found [here](https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories).

Both MET and weight can be provided in the settings dialog. The MET value is leading the activity's name. 
For example: ` 6.8 bicycling, leisure, moderate effort`

# AAT-Android and permissions

Some features like **GPS-tracking**, accessing **shared storage** and accessing **sensor-devices** need extra permission. 
This needed permissions are different on different Android versions.

AAT-Android tries to aquire all those permissions on the first application start.

To acquire permissions later open `Preferences->General/System->App permission`.
There you find an overview of missing permissions and a button to request permissions.

Some permissions can only be enabled manually in the Android settings: `App Info -> Permissions -> ... -> All permissions`

**Some important permissions**

(Naming might be different on different Android versions)

- `Location permission (allow all the time)`, `access location in the background` and `use precise location`: GPS-Tracking only works if those are enabled. If tracking stops working when AAT-Android is in the background then some of these permissions are missing.
- `Notifications (all notifications)`: To display tracker status in the top bar.
- `Read heart rate`, `connect to paired BT devices`, `discover and pair nearby BT devices` and many more: To access BT and other sensors.

**Shared storage**

On some Android versions Storage Access Framework (SAF) is needed to access shared storage locations.
SAF is token based and the token has to be renewed under some conditions like after rebooting the device.
To use SAF just select `Pick (SAF)...` in the location menu found with some preferences. Example: `Preferences->General/System/Data directory->Pick (SAF)...` and then select a directory.

On other Android versions access to shared storage can be acquired by selecting `Preferences->General/System->App permission->Request permissions` and then enable `Allow access to manage all files`

# Contribution

## Issues and feedback

- [Report an issue](https://github.com/bailuk/AAT/issues)
- [Start a discussion](https://github.com/bailuk/AAT/discussions)

## Translation

Open a pull request on GitHub to update or add translations.

### File tree structure

- Resources (like text and icons) are located in [app/src/main/res](https://github.com/bailuk/AAT/blob/stage/aat-android/src/main/res). 
- English text strings are in this file: [/app/src/main/res/values/strings.xml](https://github.com/bailuk/AAT/blob/stage/aat-android/src/main/res/values/strings.xml)
- German translations are in this file: [/app/src/main/res/values-de/strings.xml](https://github.com/bailuk/AAT/blob/stage/aat-android/src/main/res/values-de/strings.xml)

### Modify

- To add a new language just add a `value-<language code>` directory containing a `strings.xml` file.
- Use the English (original) strings.xml as a template.
- Use the same structure (order) as in the original file
- You don't have to translate everything. Just remove an entry and English is used as a fall back.
- You can modify existing strings.
- You can add new suggestions for strings.
- If a ° gets displayed somewhere inside the app it means the string is not yet in the resource file (only in java code). See [app/src/main/java/ch/bailu/aat/util/ToDo.java](https://github.com/bailuk/AAT/blob/stage/aat-lib/src/main/java/ch/bailu/aat_lib/resources/ToDo.java)
