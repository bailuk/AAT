package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.dem.tile.MultiCell;

public final class HillshadeColorTable extends ObjectHandle {

    public final static String ID=HillshadeColorTable.class.getSimpleName();

    private final static int MAX_DARKNES=50;
    private final static int TABLE_DIM=500;
    private final static int TABLE_HDIM=TABLE_DIM/2;
    private final static int TABLE_SIZE=TABLE_DIM*TABLE_DIM;

    private final static int MIN_DELTA=-250;
    private final static int MAX_DELTA=240;

    private static final int COLOR=50;
    private static final int GRAY=(COLOR << 16) | (COLOR << 8) | COLOR;

    private final byte[][] table=new byte[TABLE_DIM][TABLE_DIM];

    public HillshadeColorTable() {
        super(ID);
    }


    private boolean isInitialized=false;

    @Override
    public void onInsert(ServiceContext sc) {
        sc.getBackgroundService().process(new TableInitializer());
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }

    @Override
    public boolean isReadyAndLoaded() {
        return isInitialized;
    }


    public long getSize() {
        return TABLE_SIZE;
    }


    private static float indexToDelta(int i) {
        return (i-TABLE_HDIM) / 100f;
    }

    private static int deltaToIndex(int d) {
        return (d+TABLE_HDIM);
    }


    private static int cutDelta(int d) {
        d=Math.max(MIN_DELTA, d);
        d=Math.min(MAX_DELTA, d);
        return d;
    }


    public int getColor(final MultiCell mcell) {
        final int x=deltaToIndex(cutDelta(mcell.delta_zx()));
        final int y=deltaToIndex(cutDelta(mcell.delta_zy()));
        final int alpha=table[x][y] & 0xFF;

        return (alpha << 24) | GRAY;
    }


    private final class TableInitializer extends BackgroundTask {
        /**
         * Source:
         * http://edndoc.esri.com/arcobjects/9.2/net/shared/geoprocessing/spatial_analyst_tools/how_hillshade_works.htm
         */

        private static final double ALTITUDE_DEG=45f;

        private static final double AZIMUTH_DEG=315f;
        private static final double AZIMUTH_MATH = 360f - AZIMUTH_DEG + 90f;
        private        final double AZIMUTH_RAD = Math.toRadians(AZIMUTH_MATH);

        private static final double ZENITH_DEG=(90d-ALTITUDE_DEG);
        private        final double ZENITH_RAD=Math.toRadians(ZENITH_DEG);
        private        final double ZENITH_COS=Math.cos(ZENITH_RAD);
        private        final double ZENITH_SIN=Math.sin(ZENITH_RAD);


        private final static double DOUBLE_PI=Math.PI*2d;
        private final static double HALF_PI=Math.PI/2d;
        private final static double ONEHALF_PI=DOUBLE_PI-HALF_PI;


        @Override
        public long bgOnProcess(ServiceContext sc) {
            for (int x=0; x<TABLE_DIM; x++) {
                for (int y=0; y<TABLE_DIM; y++) {
                    table[x][y]=hillshade(indexToDelta(x), indexToDelta(y));
                }
            }

            isInitialized = true;
            AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, ID);

            return TABLE_SIZE;
        }


        private byte hillshade(float dzx, float dzy) {

            final double slope=slope_rad(dzx, dzy);

            int
                    shade = (int) (255d * (( ZENITH_COS * Math.cos(slope) ) +
                    ( ZENITH_SIN * Math.sin(slope) * Math.cos(AZIMUTH_RAD - aspect_rad(dzx, dzy)) ) ));


            shade = 255-Math.max(MAX_DARKNES, shade);


            return (byte) shade;
        }


        private double slope_rad(final double dzx, final double dzy) {
            return Math.atan(Math.sqrt(dzx*dzx + dzy*dzy));
        }


        private double aspect_rad(final double dzx, final double dzy) {
            double ret=0f;

            if (dzx!=0) {
                ret = Math.atan2(dzy, -1d*dzx);

                if (ret < 0) {

                    ret = DOUBLE_PI + ret;
                }

            } else {
                if (dzy > 0) {
                    ret = HALF_PI;

                } else if (dzy < 0) {
                    ret = ONEHALF_PI;
                }
            }
            return ret;
        }


    }


    public static final Factory FACTORY = new ObjectHandle.Factory() {

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return  new HillshadeColorTable();
        }
    };
}
