package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppTheme;


public class DownloadButton extends ViewGroup implements OnClickListener, OnLongClickListener {
    private static final int PADDING=3;
    
    private final ImageButton button;
    private final BusyIndicator busy;
    private OnClickListener listener;
    
    public DownloadButton(Context context) {
        super(context);
        button = new ImageButton(context);
        button.setImageResource(R.drawable.go_bottom);
        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
        
        AppTheme.themify(button);
        addView(button);
        
        
        busy = new BusyIndicator(context);
        busy.setPadding(PADDING, PADDING, PADDING, PADDING);
        addView(busy);
        stopWaiting();
    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        listener=l;
    }
    
    @Override
    public void onClick(View v) {
        if (listener != null) listener.onClick(this);
    }    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Above is my parents size requirement
        
        /*
         * busy indicator will have the same size as the button
         */
        
        // What is your size (calculate it!), also here is my (parents) size requirement
         button.measure(widthMeasureSpec, heightMeasureSpec);
         
         widthMeasureSpec  = MeasureSpec.makeMeasureSpec (button.getMeasuredWidth(),  MeasureSpec.EXACTLY);
         heightMeasureSpec = MeasureSpec.makeMeasureSpec (button.getMeasuredHeight(), MeasureSpec.EXACTLY);

         // I wan't you to be exactly the same size as button (and as myself)
         busy.measure(widthMeasureSpec, heightMeasureSpec);
         
         // This is my new size (from button)
         setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        button.layout(0, 0, r-l, b-t);
        busy.layout(0, 0, r-l, b-t);
    }

    
    public void startWaiting() {
        button.setVisibility(INVISIBLE);
        busy.startWaiting();
        

    }

    public void stopWaiting() {
        busy.stopWaiting();
        button.setVisibility(VISIBLE);
    }

    public boolean isWaiting() {
        return busy.isWaiting();
    }


    @Override
    public boolean onLongClick(View v) {
        return performLongClick();
    }


}
