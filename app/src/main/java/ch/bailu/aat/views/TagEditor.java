package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;


public class TagEditor extends LinearLayout {
    private EditText editor;
    private File backup;
    
    
    public TagEditor(Context context, String an)  {
        super(context);
        
        createEditor();
        createEditorBackup(an);

        AppBroadcaster.register(getContext(), 
                onSelectMapFeature, 
                AppBroadcaster.SELECT_MAP_FEATURE);
    }

    private void createEditorBackup(String directory) {
        backup = new File(directory,"edit.txt");
        showFile();
    }


    private void createEditor() {
        int cw,ch;

        editor = new EditText(getContext());
        editor.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE 
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        editor.setHorizontallyScrolling(true);

        if (AppLayout.getOrientation(getContext())== Configuration.ORIENTATION_LANDSCAPE) {
            cw = AppLayout.getScreenLargeSide(getContext())/2;
            ch=ViewGroup.LayoutParams.MATCH_PARENT;
            
        } else {
            ch = AppLayout.getScreenLargeSide(getContext())/5;
            cw=ViewGroup.LayoutParams.MATCH_PARENT;
        }
        
        addView(editor, cw, ch);
    }


    private final BroadcastReceiver onSelectMapFeature = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = AppIntent.getFile(intent);
            
            if (text != null) {
                editor.append("\n");
                editor.append(text);
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


    public String getText() {
        return editor.getText().toString();
    }
    


    
    private void showFile() {
        String text;
        try {
            text = TextBackup.read(backup);
            
        } catch (IOException e) {
            text = "";
        }
        editor.setText(text);
        
    }

    
    private void saveFile() {
        try {
            TextBackup.write(backup, getText());
        } catch (IOException e) {
            AppLog.e(getContext(), TagEditor.this, e);
            
        }
    }
    
    public void erase() {
        editor.setText("");
    }

    public void setText(String query) {
        editor.setText(query);
    }
}
