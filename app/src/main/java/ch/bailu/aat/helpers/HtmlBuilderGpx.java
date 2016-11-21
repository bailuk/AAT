package ch.bailu.aat.helpers;


import android.content.Context;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.SpeedDescription;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxType;

public class HtmlBuilderGpx extends HtmlBuilder {

    private final DistanceDescription distance;
    private final SpeedDescription speed;
    private final AltitudeDescription altitude;


    public HtmlBuilderGpx(Context context) {
        distance = new DistanceDescription(context);
        speed = new CurrentSpeedDescription(context);
        altitude = new AltitudeDescription(context);
    }

    public void appendInfo(GpxInformation info, int index) {
        final int count=index+1, total = info.getGpxList().getPointList().size();

        appendHeader(info.getName());
        append("<b>" + count + "</b>/" + total + "<br>");
    }

    public void appendNode(GpxPointNode n, GpxInformation i) {
        if (i.getType() == GpxType.TRK && n.getTimeStamp() != 0 ) {
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
        if (a.size()>0) {

            for (int i = 0; i < a.size(); i++) {
                String k = a.getKey(i);
                String v = a.getValue(i);

                if (k.contains("name")) {
                    appendKeyValueBold(k, v);
                } else {
                    appendKeyValue(k, v);
                }
                append("<br>");
            }
        }
    }

}
