package ch.bailu.aat.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageButton;
import ch.bailu.aat.R;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppTheme;

public class BusyButton extends ViewGroup   {
    
    
    private final ImageButton button;
    private final BusyIndicator busy;
    
    public BusyButton(Context context, int image_res) {
        super(context);
        
        button = new ImageButton(context);
        button.setImageResource(image_res);
        button.setClickable(false);
        AppTheme.themify(button);
        addView(button);        
        

        busy = new BusyIndicator(context);
        busy.setClickable(false);
        busy.setBackgroundResource(R.drawable.button);
        addView(busy);

        stopWaiting();
        setBackgroundResource(R.drawable.button);
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
        busy.startWaiting();
    }

    
    public void stopWaiting() {
        busy.stopWaiting();
    }

    public boolean isWaiting() {
        return busy.isWaiting();
    }

    


    public BusyControl getBusyControl(int id) {
        return new BusyControl(id);
    }
    
    public class BusyControl implements DescriptionInterface {
        private final int ID;

        public BusyControl(int iid) {
            ID=iid;
        }
        
        
        @Override
        public void updateGpxContent(GpxInformation info) {
            if (info.getID()==ID) {
                if (info.isLoaded()) {
                    stopWaiting();
                } else {
                    startWaiting();
                }
                
            }
        }
    }
}
