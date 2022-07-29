#!/bin/bash

# Convert images to android resources and copies them to 'assets' directory
# Depends on 'xcf2png' (https://github.com/j-jorge/xcftools/)
# and 'convert' from ImageMagick (https://imagemagick.org/index.php)

DRAW="../../app/src/main/res/drawable"

DRAWABLES="$DRAW/"
LDPI="$DRAW-ldpi/"
MDPI="$DRAW-mdpi/"
HDPI="$DRAW-hdpi/"
XHDPI="$DRAW-xhdpi/"
XXHDPI="$DRAW-xxhdpi/"
XXXHDPI="$DRAW-xxxhdpi/"


function SVG_COPY {
    TARGET="${1//-/_}".png

    echo "convert $1.svg (svg)"
	convert -size "36x36" $1.svg $LDPI/$TARGET
	convert -size "48x48" $1.svg $MDPI/$TARGET
	convert -size "72x72" $1.svg $HDPI/$TARGET
	convert -size "96x96" $1.svg $XHDPI/$TARGET
	convert -size "144x144" $1.svg $XXHDPI/$TARGET
	convert -size "192x192" $1.svg $XXXHDPI/$TARGET

}


function COPY {
    TARGET="${1//-/_}".png

    echo "convert $1.png (big)"
	convert $1.png -scale "36x36"   $LDPI/$TARGET
	convert $1.png -scale "48x48"   $MDPI/$TARGET
	convert $1.png -scale "72x72"   $HDPI/$TARGET
	convert $1.png -scale "96x96"   $XHDPI/$TARGET
	convert $1.png -scale "144x144" $XXHDPI/$TARGET
	convert $1.png -scale "192x192" $XXXHDPI/$TARGET

}


function SCOPY {
    TARGET="${1//-/_}".png

    echo "convert $1.png (small)"
	convert $1.png -scale "18x18"   $LDPI/$TARGET
	convert $1.png -scale "24x24"   $MDPI/$TARGET
	convert $1.png -scale "36x36"   $HDPI/$TARGET
	convert $1.png -scale "48x48"   $XHDPI/$TARGET
	convert $1.png -scale "72x72" $XXHDPI/$TARGET
	convert $1.png -scale "96x96" $XXXHDPI/$TARGET

}


function INVERT {
	TARGET="${1//-/_}_inverse.png"

    echo "convert $1.png (invert)"
	convert $1.png -negate -scale "36x36"   $LDPI/$TARGET
	convert $1.png -negate -scale "48x48"   $MDPI/$TARGET
	convert $1.png -negate -scale "72x72"   $HDPI/$TARGET
	convert $1.png -negate -scale "96x96"   $XHDPI/$TARGET
	convert $1.png -negate -scale "144x144" $XXHDPI/$TARGET
	convert $1.png -negate -scale "192x192" $XXXHDPI/$TARGET

}


function HIGHLIGHT {
	TARGET="${1//-/_}_highlight.png"

    echo "convert $1.png (highlight)"
	convert $1.png -negate -scale "36x36"   $LDPI/$TARGET
	convert $1.png -negate -scale "48x48"   $MDPI/$TARGET
	convert $1.png -negate -scale "72x72"   $HDPI/$TARGET
	convert $1.png -negate -scale "96x96"   $XHDPI/$TARGET
	convert $1.png -negate -scale "144x144" $XXHDPI/$TARGET
	convert $1.png -negate -scale "192x192" $XXXHDPI/$TARGET

}

function CCONVERT {
    echo "xcf2png $1.xcf (local)"
	xcf2png $1.xcf > $1.png
	COPY $1
}


function CONVERT {
    echo "xcf2png $1.xcf (to resources)"
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
CLEAR_DIRECTORY $XXHDPI
CLEAR_DIRECTORY $XXXHDPI


####### Application
SCOPY "status"

####### Preview
COPY "open-menu-light"

####### Top-Bar
INVERT "edit-undo"
INVERT "go-next"
INVERT "go-previous"
INVERT "go-down"
INVERT "go-up"
INVERT "open-menu"
INVERT "folder"
INVERT "edit-select-all"


####### Navigation-Bar
COPY "zoom-fit-best"
COPY "zoom-in"
COPY "zoom-out"
COPY "zoom-original"
INVERT "zoom-original"

####### File-Bar
COPY "view-paged"

####### Edit-Bar
COPY "go-up"
COPY "go-down"
COPY "edit-undo"
COPY "user-trash"
COPY "edit-redo"
COPY "list-add"
COPY "list-remove"

####### Information-Bar
COPY "open-menu"
COPY "view-grid"
COPY "dialog-question"
COPY "go-bottom"  ## Overpass
COPY "edit-find"  ## Nominatim
COPY "view-refresh"
COPY "find-location"

####### Tile-Remover
INVERT "view-refresh"
INVERT "user-trash"

####### Tracker / Menu
INVERT "media-playback-pause"
INVERT "media-playback-start"
INVERT "media-playback-stop"

####### FileView / OsmApi Activities
INVERT "view-paged"
INVERT "go-bottom"  ## Overpass
INVERT "edit-clear-all"
INVERT "document-save-as"
