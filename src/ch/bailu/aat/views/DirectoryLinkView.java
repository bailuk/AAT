package ch.bailu.aat.views;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppTheme;

public class DirectoryLinkView extends TextView implements OnClickListener {

    public DirectoryLinkView(Context context, File directory) {
        super(context);
        
        setTextColor(AppTheme.getHighlightColor());
        setTextSize(20);
        setText(directory.toString());
        setBackgroundResource(R.drawable.button);
        
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==this) {
            File directory = new File(getText().toString());
            
            if (directory.exists()) {
            
                final Uri uri = Uri.fromFile(directory);
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                //intent.setDataAndType(uri, "resource/folder");
                //intent.setDataAndType(uri, "text/csv");

                getContext().startActivity(Intent.createChooser(intent, directory.toString()));
                
                /*
                if (intent.resolveActivityInfo(getContext().getPackageManager(), 0) != null) {
                    getContext().startActivity(intent);
                    
                } else {
                  // if you reach this place, it means there is no any file 
                  // explorer app installed on your device
                }
                */
            }
        }
    }
    

}
