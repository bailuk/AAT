#!/bin/bash

DRAW="../res/drawable"

DRAWABLES="$DRAW/"
LDPI="$DRAW-ldpi/"
MDPI="$DRAW-mdpi/"
HDPI="$DRAW-hdpi/"
XHDPI="$DRAW-xhdpi/"


function COPY {
    TARGET="${1//-/_}".png

	convert $1.png -scale "36x36" $LDPI/$TARGET
	convert $1.png -scale "48x48" $MDPI/$TARGET
	convert $1.png -scale "72x72" $HDPI/$TARGET
	convert $1.png -scale "96x96" $XHDPI/$TARGET
}


function INVERT {
	TARGET="${1//-/_}_inverse.png"

	convert $1.png -negate -scale "36x36" $LDPI/$TARGET
	convert $1.png -negate -scale "48x48" $MDPI/$TARGET
	convert $1.png -negate -scale "72x72" $HDPI/$TARGET
	convert $1.png -negate -scale "96x96" $XHDPI/$TARGET
}


function HIGHLIGHT {
	TARGET="${1//-/_}_highlight.png"

	convert $1.png -negate -scale "36x36" $LDPI/$TARGET
	convert $1.png -negate -scale "48x48" $MDPI/$TARGET
	convert $1.png -negate -scale "72x72" $HDPI/$TARGET
	convert $1.png -negate -scale "96x96" $XHDPI/$TARGET
}



function CONVERT {
	xcf2png $1.xcf > $DRAWABLES/$1.png
}



function CLEAR_DIRECTORY {
    if [ ! -d "$1" ]; then
        echo "$1 does not exist."
        exit 1
    fi

    echo "Remove all Files in $1"
    rm $1*
}

CLEAR_DIRECTORY $DRAWABLES
CLEAR_DIRECTORY $LDPI
CLEAR_DIRECTORY $MDPI
CLEAR_DIRECTORY $HDPI
CLEAR_DIRECTORY $XHDPI



####### Global
cp button.xml $DRAWABLES
CONVERT "button_default.9"
CONVERT "button_pressed.9"
CONVERT "button_selected.9"

####### Application
CONVERT "icon" 
CONVERT "status"


####### Top-Bar
INVERT "go-next" 
INVERT "go-previous" 
INVERT "go-down" 
INVERT "go-up" 
INVERT "open-menu"
INVERT "edit-select-all"
INVERT "folder"


####### Navigation-Bar
COPY "zoom-fit-best" 
COPY "zoom-in" 
COPY "zoom-out" 
COPY "zoom-original" 
INVERT "zoom-original"

####### File-Bar
COPY "edit-select-all"
COPY "view-paged"


####### Edit-Bar
COPY "go-up" 
COPY "go-down" 
COPY "document-save-as" 
COPY "edit-clear-all" 
COPY "document-save" 
COPY "edit-undo" 
COPY "user-trash" 
COPY "edit-redo" 
COPY "list-add" 
COPY "list-remove" 
COPY "gtk-convert" 


####### Invormation-Bar
COPY "view-grid" 
COPY "dialog-question" 
COPY "go-bottom"  ## Overpass
COPY "edit-find"  ## Nominatim
COPY "view-refresh"  
COPY "find-location"


####### Tracker / Menu
INVERT "media-playback-pause" 
INVERT "media-playback-start" 
INVERT "media-playback-stop" 



####### FileView / OsmApi Activities
INVERT "view-paged"
INVERT "content-loading"
INVERT "go-bottom"  ## Overpass
INVERT "edit-find"  ## Nominatim
INVERT "edit-clear-all" 
INVERT "document-save-as" 

