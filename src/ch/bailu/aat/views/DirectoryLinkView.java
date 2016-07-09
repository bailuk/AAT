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
            
            if (directory.exists()) {  // this works (only?) with OI Filemanager
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                final Uri uri = Uri.fromFile(directory);
                
                intent.setData(uri);
                getContext().startActivity(Intent.createChooser(intent, directory.toString()));
            }
        }
    }
}
