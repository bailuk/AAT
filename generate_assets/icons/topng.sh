#!/bin/bash


function png_mres {
	target="${1//-/_}"

	convert $1.png -scale $2 ../res/drawable-ldpi/$target.png
	convert $1.png -scale $3 ../res/drawable-mdpi/$target.png
	convert $1.png -scale $4 ../res/drawable-hdpi/$target.png
	convert $1.png -scale $5 ../res/drawable-xhdpi/$target.png
}


function png_mnres {
	target="${1//-/_}"

	convert $1.png -negate -scale $2 ../res/drawable-ldpi/$target.png
	convert $1.png -negate -scale $3 ../res/drawable-mdpi/$target.png
	convert $1.png -negate -scale $4 ../res/drawable-hdpi/$target.png
	convert $1.png -negate -scale $5 ../res/drawable-xhdpi/$target.png
}


function mres {
	xcf2png $1.xcf > $1.png
	png_mres $1 $2 $3 $4 $5
	rm $1.png
}


function sres {
	xcf2png $1.xcf > ../res/drawable/$1.png
}  


rm ../res/drawable-ldpi/* ../res/drawable-mdpi/* ../res/drawable-hdpi/* ../res/drawable-xhdpi/*

sres "button_default.9"
sres "button_pressed.9"
sres "button_selected.9"


mres "icon" "36x36" "48x48" "72x72" "96x96"   
mres "status" "12x12" "24x24" "36x36" "48x48"

button_size="24x24 36x36 48x48 72x72"

png_mnres "go-next" $button_size
png_mnres "go-down" $button_size

png_mnres "zoom-fit-best" $button_size
png_mnres "zoom-in" $button_size
png_mnres "zoom-out" $button_size
png_mnres "zoom-original" $button_size


mres "layer" $button_size
png_mres "document-save-as" $button_size

png_mnres "view-grid" $button_size

png_mres "edit-clear-all" $button_size
png_mres "document-new" $button_size
png_mres "document-save" $button_size
png_mres "go-bottom" $button_size
png_mres "edit-find" $button_size
#png_mres "system-search1" $button_size
png_mres "view-refresh" $button_size
png_mres "go-previous" $button_size
png_mres "go-up" $button_size

png_mres "media-playback-pause" $button_size
png_mres "media-playback-start" $button_size
png_mres "media-playback-stop" $button_size
png_mres "list-add" $button_size
png_mres "list-remove" $button_size
png_mres "gtk-convert" $button_size

png_mres "edit-undo" $button_size
png_mres "user-trash" $button_size
png_mres "edit-redo" $button_size
png_mres "dialog-question" $button_size

png_mnres "display-brightness" $button_size

