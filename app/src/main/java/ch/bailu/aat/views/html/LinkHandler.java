package ch.bailu.aat.views.html;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

public abstract class LinkHandler {

    public Spannable convertAll(Spanned original) {
        SpannableString result=new SpannableString(original);
        URLSpan[] spans=result.getSpans(0, result.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start=result.getSpanStart(span);
            int end=result.getSpanEnd(span);
            int flags=result.getSpanFlags(span);

            result.removeSpan(span);
            result.setSpan(new ConvertedClickableSpan(span), start, end, flags);
        }

        return(result);
    }


    private class ConvertedClickableSpan extends ClickableSpan {

        private final URLSpan urlSpan;
        public ConvertedClickableSpan(URLSpan span) {
            urlSpan = span;
        }
        @Override
        public void onClick(@NonNull View view) {
            if (openLink(urlSpan.getURL()) == false)
                urlSpan.onClick(view);
        }
    }


    public abstract boolean openLink(String url);
}
