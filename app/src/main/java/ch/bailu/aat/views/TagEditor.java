package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.widget.EditText;

import java.io.IOException;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;


public class TagEditor extends EditText {
    private Foc backup;
    
    
    public TagEditor(Context context, Foc an)  {
        super(context);
        
        createEditor();
        createEditorBackup(an);

        AppBroadcaster.register(getContext(), 
                onSelectMapFeature, 
                AppBroadcaster.SELECT_MAP_FEATURE);
    }

    private void createEditorBackup(Foc directory) {
        backup = directory.child("edit.txt");
        showFile();
    }


    private void createEditor() {
        setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        setHorizontallyScrolling(true);
    }


    private final BroadcastReceiver onSelectMapFeature = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = AppIntent.getFile(intent);
            
            if (text != null) {
                append("\n");
                append(text);
                AppLog.i(getContext(), text);
            }
        }
    };
    
    
    @Override
    public void onDetachedFromWindow() {
        saveFile();
        getContext().unregisterReceiver(onSelectMapFeature);
        super.onDetachedFromWindow();
    }


    private void showFile() {
        String text;
        try {
            text = TextBackup.read(backup);
            
        } catch (IOException e) {
            text = "";
        }
        setText(text);
        
    }

    
    private void saveFile() {
        try {
            TextBackup.write(backup, getText().toString());
        } catch (IOException e) {
            AppLog.e(getContext(), TagEditor.this, e);
            
        }
    }
    
    public void erase() {
        setText("");
    }
}
