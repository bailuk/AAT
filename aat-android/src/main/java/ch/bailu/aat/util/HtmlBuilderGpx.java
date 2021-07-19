package ch.bailu.aat.util;


import android.content.Context;

import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.SpeedDescription;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;


public class HtmlBuilderGpx extends HtmlBuilder {

    private final DistanceDescription distance;
    private final SpeedDescription speed;
    private final AltitudeDescription altitude;


    public HtmlBuilderGpx(Context context) {
        distance = new DistanceDescription(new Storage(context));
        speed = new CurrentSpeedDescription(new Storage(context));
        altitude = new AltitudeDescription(new Storage(context));
    }

    public void appendInfo(GpxInformation info, int index) {
        final int count=index+1, total = info.getGpxList().getPointList().size();

        appendHeader(info.getFile().getName());
        append("<b>" + count + "</b>/" + total + "<br>");
    }

    public void appendNode(GpxPointNode n, GpxInformation i) {
        if (i.getType() == GpxType.TRACK && n.getTimeStamp() != 0 ) {
            append(speed.getLabel(), speed.getSpeedDescription(n.getSpeed()));
            append("<br>");
        }

        append(altitude.getLabel(), distance.getAltitudeDescription(n.getAltitude()));
        append("<br>");

    }


    public void append(ContentDescription d) {
        append(d.getLabel(), d.getValueAsString());
    }



    public void appendAttributes(GpxAttributes a) {
        if (a.size() > 0) {

            for (int i = 0; i < a.size(); i++) {
                int k = a.getKeyAt(i);
                String kString = a.getSKeyAt(i);
                String v = a.getAt(i);

                if (kString.toLowerCase().contains("name")) {
                    appendKeyValueBold(kString, v);
                }  else {
                    appendKeyValue(kString, v);
                }
                append("<br>");
            }
        }
    }

}
