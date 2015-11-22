package ch.bailu.aat.activities;

import android.os.Bundle;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;

public class StatusActivity extends AbsHtmlViewActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToServices(MultiServiceLink.ALL_SERVICES);
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

        for (Class<?> s: MultiServiceLink.ALL_SERVICES) {
            try {
                getService(s).appendStatusText(content);
            } catch (ServiceNotUpException e) {
                content.append("<p>ERROR*: ");
                content.append(e.getMessage());
                content.append("</p>");
            }
        }
        return content.toString();
    }

}
