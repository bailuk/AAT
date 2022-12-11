#!/bin/sh

proguard -ignorewarnings  -keep  class ch.bailu.aat_gtk** -injars aat-gtk-all.jar -outjars out.jar
