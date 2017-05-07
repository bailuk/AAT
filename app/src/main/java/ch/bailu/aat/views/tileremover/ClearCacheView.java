package ch.bailu.aat.views.tileremover;

import android.app.Activity;
import android.view.View;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.ImageButtonView;
/*
public class ClearCacheView  extends ImageButtonView {


        public ClearCacheView(final Activity acontext, String cacheName) {
            super(acontext, R.drawable.user_trash_inverse);

            final File file = getCacheDirectory(cacheName);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }




        public File getCacheDirectory(String name) {
            return new File(
                    new SolidTileCacheDirectory(getContext()).getValueAsFile(),
                    name);
        }


    }

*/