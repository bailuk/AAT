package ch.bailu.aat.views;

import android.content.Context;
import android.text.util.Linkify;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.aat.views.preferences.VerticalScrollView;
import ch.bailu.util_java.foc.Foc;


public class HtmlScrollTextView extends VerticalScrollView {
    private final HtmlTextView textView;

    public HtmlScrollTextView(Context context) {
        super(context);

        textView = new HtmlTextView(context);
        add(textView);
    }


    public HtmlScrollTextView(Context context, Foc file) {
        this(context, toString(file));
    }


    public static String toString(Foc file) {

        StringBuilder builder = new StringBuilder((int) file.length()+1);
        InputStream in = null;

        try {
            in = new BufferedInputStream(file.openR());

            int b;

            while ((b = in.read()) > -1) {
                char c = (char) b;
                builder.append(c);
            }

        } catch (IOException e) {
            builder.append(e.getMessage());
        } finally {
            Foc.close(in);
        }

        return builder.toString();
    }


    public HtmlScrollTextView(Context context, int res) {
        this(context, context.getString(res));
    }


    public HtmlScrollTextView(Context context, String text) {
        this(context);
        setHtmlText(text);
    }


    public void setHtmlText(String text) {

        textView.setHtmlText(text);
    }

    public void enableAutoLink() {
        textView.setAutoLinkMask(Linkify.ALL);
    }

    public HtmlTextView getTextView() {
        return textView;
    }
}
