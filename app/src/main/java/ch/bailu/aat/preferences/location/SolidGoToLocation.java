package ch.bailu.aat.preferences.location;

import android.content.Context;
import android.view.View;

import com.google.openlocationcode.OpenLocationCode;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.Coordinates;
import ch.bailu.aat.exception.ValidationException;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.preferences.SolidTextInputDialog;

public class SolidGoToLocation extends SolidString {
    private final static String KEY = "GoToLocation";
    public SolidGoToLocation(Context c) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Center map at location (Geo URL or plus code):");
    }


    public void goToLocationFromUser(MapViewInterface map) {
        new SolidTextInputDialog(this, SolidTextInputDialog.TEXT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation(map, getValueAsString());
            }
        });
    }

    public static void goToLocation(MapViewInterface map, String s) {
        try {
            map.setCenter(latLongFromString(s));

        } catch (Exception e) {
            AppLog.e(map.getMContext().getContext(), e);
        }

    }


    private static LatLong latLongFromString(String s)
            throws  IllegalArgumentException, IllegalStateException {

        try {
            OpenLocationCode code = new OpenLocationCode(s);

            OpenLocationCode.CodeArea a = code.decode(s);

            return new LatLong(a.getCenterLatitude(), a.getCenterLongitude());

        } catch (Exception exception) {
            try {
                return Coordinates.stringToGeoPoint(s);

            } catch (NumberFormatException e) {
                throw exception;
            }
        }
    }


    @Override
    public void setValueFromString(String s) throws ValidationException {
        s = s.trim();


        if (! validate(s)) {
            throw new ValidationException(ToDo.translate("Geo URL or plus code"));
        } else {
            setValue(s);
        }
    }


    @Override
    public boolean validate(String s) {
        try  {
            return latLongFromString(s) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
