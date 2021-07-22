package ch.bailu.aat_awt.views;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class JNumberView extends JPanel implements OnContentUpdatedInterface {

    private final JLabel    label, number, unit;
    private final ContentDescription description;

    public JNumberView(ContentDescription data) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        label = createLabel();
        number = createLabel();
        unit = createLabel();
        setDefaultUnitLabelColor();

        description=data;
        updateAllText();

        number.setFont(label.getFont().deriveFont(30f));

    }

    public void setDefaultUnitLabelColor() {
    }

    public ContentDescription getDescription() {
        return description;
    }



    private JLabel createLabel() {
        JLabel view = new JLabel();

        add(view);
        return view;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        description.onContentUpdated(iid, info);
        updateAllText();
    }


    public void updateAllText() {
        number.setText(description.getValue());
        label.setText(description.getLabelShort());
        unit.setText(description.getUnit());
    }
}
