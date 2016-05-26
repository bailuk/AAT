package ch.bailu.aat.activities;

import android.os.Bundle;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.ServiceLink;

public class StatusActivity extends AbsHtmlViewActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToServices(ServiceLink.ALL_SERVICES);
    }


    @Override
    public void onServicesUp() {
        setText(0);
    }


    @Override
    public String getText() {
        StringBuilder content = new StringBuilder();

        appendStatusText(content);
        AbsService.appendStatusTextStatic(content);
        getServiceContext().appendStatusText(content);
        
        return content.toString();
    }

}
