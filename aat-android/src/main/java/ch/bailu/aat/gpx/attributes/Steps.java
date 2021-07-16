package ch.bailu.aat.gpx.attributes;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.services.sensor.attributes.StepCounterAttributes;

public class Steps extends GpxSubAttributes {
    private int total = 0;
    private int counting = 0;


    public Steps() {
        super(new Keys(StepCounterAttributes.KEY_INDEX_STEPS_RATE,
                StepCounterAttributes.KEY_INDEX_STEPS_TOTAL));
    }

    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {
        final GpxAttributes attr = p.getAttributes();


        if (attr.hasKey(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL)) {
            final int value = attr.getAsInteger(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL);

            if (value < counting) {
                total += counting;
            }

            counting = value;
        }

        return autoPause;
    }

    @Override
    public String get(int keyIndex) {
        return String.valueOf(getAsInteger(keyIndex));
    }


    @Override
    public int getAsInteger(int keyIndex) {
        if (keyIndex == StepCounterAttributes.KEY_INDEX_STEPS_TOTAL) {
            return counting + total;

        } else if (keyIndex == StepCounterAttributes.KEY_INDEX_STEPS_RATE) {
            return 0;
        }
        return 0;
    }
}
