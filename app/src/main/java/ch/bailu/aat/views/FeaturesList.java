package ch.bailu.aat.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.HtmlViewActivity;
import ch.bailu.aat.activities.MapFeatureListActivity;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppHtml;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.helpers.file.FileAccess;
import ch.bailu.aat.osm_features.MapFeaturesParser;
import ch.bailu.aat.osm_features.MapFeaturesParser.OnHaveFeature;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.icons.IconMapService;

public class FeaturesList extends ListView implements 
OnHaveFeature { 

    private DataSetObserver observer=null;

    private final ServiceContext scontext;

    private class ListData {
        public String name, key, value;
        public Spanned paragraph;
    }


    private final ArrayList<ListData> data = new ArrayList<>();


    public FeaturesList(ServiceContext sc, FileAccess file) {
        this(sc);

        loadList(file, sc.getIconMapService());
    }

    public FeaturesList(ServiceContext sc) {
        super(sc.getContext());

        final Adapter listAdapter = new Adapter();

        scontext = sc;
        AppTheme.themify(this, AppTheme.getHighlightColor());

        setAdapter(listAdapter);
        setOnItemClickListener(listAdapter);
        setOnItemLongClickListener(listAdapter);

    }


    public void loadList() {
        try {
            File[] files=AppDirectory.getDataDirectory(getContext(), AppDirectory.DIR_OSM_FEATURES_PREPARSED).listFiles();

            if (files != null) {
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File a, File b) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                new MapFeaturesParser(this, files);

                if (observer != null) observer.onChanged();
            }
        } catch (IOException e) {
            AppLog.e(getContext(), this, e);
        }
    }



    public void loadList(FileAccess file, IconMapService.Self map) {
        try {
            new MapFeaturesParser(this, file);
            if (observer != null) observer.onChanged();

        } catch (IOException e) {
            AppLog.e(getContext(), this, e);
        }

    }


    @Override
    public void onHaveFeature(MapFeaturesParser parser) {
        ListData d = new ListData();

        d.name = parser.getName();
        d.key = parser.getKey();
        d.value = parser.getValue();

        StringBuilder html=new StringBuilder();
        scontext.getIconMapService().iconify(html,parser.getKey(), parser.getValue());
        parser.toHtml(html);

        d.paragraph = AppHtml.fromHtml(html.toString(), new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {

                Bitmap bitmap = BitmapFactory.decodeFile(source);
                if (bitmap != null) {
                    Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                    drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    return drawable;

                } else {
                    return null;
                }

            }
        });

        data.add(d);        
    }







    private void broadcastKeyValue(CharSequence key, CharSequence value) {
        String text = "["+key+"="+value+"]";
        AppBroadcaster.broadcast(getContext(), AppBroadcaster.SELECT_MAP_FEATURE, text);
    }


    private void startFeatureListActivity(CharSequence name) {
        try {
            File file = new File(AppDirectory.getDataDirectory(getContext(), AppDirectory.DIR_OSM_FEATURES_PREPARSED),name.toString());

            if (file.exists()) {
                ActivitySwitcher.start(getContext(), MapFeatureListActivity.class, file);
            }

        } catch (Exception e) {
            AppLog.e(getContext(), this, e);
        }

    }


    private class Adapter implements ListAdapter, android.widget.AdapterView.OnItemClickListener,
    android.widget.AdapterView.OnItemLongClickListener{


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



        @Override
        public boolean onItemLongClick(AdapterView<?> v, View v1, int index,
                long id) {
            ListData d = data.get(index);

            if (d.name.length()>1) {

                File file;
                try {
                    file = new File(AppDirectory.getDataDirectory(getContext(), AppDirectory.DIR_OSM_FEATURES_PREPARSED), d.name);
                    String content = new FileAccess(file).contentToString();
                    ActivitySwitcher.start(getContext(), HtmlViewActivity.class, content);

                } catch (Exception e) {
                    AppLog.e(getContext(), this, e);
                }
            }

            return false;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
            ListData d = data.get(index);

            if (d.name.length()>1) {
                startFeatureListActivity(d.name);

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
