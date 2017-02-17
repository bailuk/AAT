package ch.bailu.aat.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.MapFeatureListActivity;
import ch.bailu.aat.osm_features.MapFeaturesParser;
import ch.bailu.aat.osm_features.MapFeaturesParser.OnHaveFeature;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppHtml;
import ch.bailu.aat.util.fs.AssetAccess;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.simpleparser.AbsAccess;

public class FeaturesList extends ListView  {

    private static final String MAP_FEATURES_ASSET = "map_features";
    private DataSetObserver observer=null;

    private final int icon_size;//, sicon_size;

    private final Drawable way, node, area, relation;

    private static class ListData {
        public String name, key, value;
        public Spanned paragraph;
    }

    private final ArrayList<ListData> data = new ArrayList<>();


    public FeaturesList(Context context) {
        super(context);

        icon_size = new AppDensity(context).toDPi(64);
        int sicon_size = new AppDensity(context).toDPi(24);

        way = toDrawable("symbols/way.svg", sicon_size, Color.WHITE);
        node = toDrawable("symbols/node.svg", sicon_size, Color.WHITE);
        area = toDrawable("symbols/area.svg", sicon_size, Color.WHITE);
        relation = toDrawable("symbols/relation.svg", sicon_size, Color.WHITE);

        final Adapter listAdapter = new Adapter();

        AppTheme.themify(this, AppTheme.getHighlightColor());

        setAdapter(listAdapter);
        setOnItemClickListener(listAdapter);
        //setOnItemLongClickListener(listAdapter);

    }





    public class ListLoader implements OnHaveFeature {
        private final IconMapService map;

        public ListLoader(IconMapService m) {
            map = m;
        }


        public void loadList(AssetManager assets) throws IOException {
            ArrayList<String> files = AssetAccess.listAssets(assets, MAP_FEATURES_ASSET);

            Collections.sort(files);
            new MapFeaturesParser(assets, this, files);
        }


        public void loadList(AbsAccess file) throws IOException {
            new MapFeaturesParser(this, file);
        }


        @Override
        public void onHaveFeature(MapFeaturesParser parser) {
            ListData d = new ListData();

            d.name = parser.getName();
            d.key = parser.getKey();
            d.value = parser.getValue();

            StringBuilder html=new StringBuilder();
            map.iconify(html,parser.getKey(), parser.getValue());
            parser.toHtml(html);

            d.paragraph = AppHtml.fromHtml(html.toString(), new Html.ImageGetter() {

                @Override
                public Drawable getDrawable(String source) {

                    if (source.endsWith(".svg")) {
                        return toDrawable(source, icon_size, Color.TRANSPARENT);

                    } else if (source.contains("way")) {
                        return way;

                    } else if (source.contains("node")) {
                        return node;

                    } else if (source.contains("area")) {
                        return area;

                    } else if (source.contains("relation")) {
                        return relation;

                    } else {
                        AppLog.d(this, source);
                    }


                    return null;
                }
            });

            data.add(d);
        }

    }

    private Drawable toDrawable(String asset, int size, int color) {
        try {
            SVG svg = SVG.getFromAsset(getContext().getAssets(), asset);
            return SyncTileBitmap.toDrawable(svg, size, getResources(), color);

        } catch (SVGParseException | IOException e) {
            AppLog.d(this, asset);
        }
        return null;
    }

    public void loadList(AssetManager assets, IconMapService map) {
        try {
            new ListLoader(map).loadList(assets);
            if (observer != null) observer.onChanged();
        } catch (IOException e) {
            AppLog.e(getContext(), this, e);
        }
    }



    public void loadList(AbsAccess file, IconMapService map) {
        try {
            new ListLoader(map).loadList(file);
            if (observer != null) observer.onChanged();
        } catch (IOException e) {
            AppLog.e(getContext(), this, e);
        }

    }









    private void broadcastKeyValue(CharSequence key, CharSequence value) {
        String text = "["+key+"="+value+"]";
        AppBroadcaster.broadcast(getContext(), AppBroadcaster.SELECT_MAP_FEATURE, text);
    }


    private void startFeatureListActivity(CharSequence name) {
            ActivitySwitcher.start(getContext(), MapFeatureListActivity.class, name.toString());
    }


    private class Adapter implements ListAdapter, android.widget.AdapterView.OnItemClickListener{
//            android.widget.AdapterView.OnItemLongClickListener{


        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public View getView(int index, View v, ViewGroup p) {
            TextView text = (TextView) v;
            if (text==null) {
                text = new TextView(getContext());
            }
            text.setText(data.get(index).paragraph);
            return text;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver o) {
            observer=o;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver o) {
            observer = null;
        }



//        @Override
//        public boolean onItemLongClick(AdapterView<?> v, View v1, int index,
//                                       long id) {
//            ListData d = data.get(index);
//
//            if (d.name.length()>1) {
//
//                String file;
//                try {
//                    file = MAP_FEATURES_ASSET+ "/"+d.name;
//                    String content = new AssetAccess(file).contentToString();
//                    ActivitySwitcher.start(getContext(), HtmlViewActivity.class, content);
//
//                } catch (Exception e) {
//                    AppLog.e(getContext(), this, e);
//                }
//            }
//
//            return false;
//        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
            ListData d = data.get(index);

            if (d.name.length()>1) {
                startFeatureListActivity(MAP_FEATURES_ASSET+ "/"+d.name);

            } else if (d.key.length()>1
                    && d.value.length()>1) {
                broadcastKeyValue(d.key, d.value);
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public int getItemViewType(int position) {
            return 0;
        }


        @Override
        public int getViewTypeCount() {
            return 1;
        }


        @Override
        public boolean hasStableIds() {
            return false;
        }


        @Override
        public boolean isEmpty() {
            return getCount()==0;
        }


        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }


        @Override
        public boolean isEnabled(int index) {
            return true;
        }

    }
}
