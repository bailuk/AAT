package ch.bailu.aat.map.layer.control;

import android.view.View;

import ch.bailu.aat.dispatcher.EditorSourceInterface;
import ch.bailu.aat.services.cache.ObjGpxEditable;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.foc.Foc;

public final class EditorNodeViewLayer extends NodeViewLayer {

    private final EditorSourceInterface editorSource;
    private boolean showNode = false;

    public EditorNodeViewLayer(ServicesInterface services, StorageInterface s, MapContext mc, EditorSourceInterface e) {
        super(services, s, mc);
        editorSource = e;

        setText();


    }

    private void setText() {
        if (editorSource.isEditing()) {
            if (!showNode) setNoNodeSelectedText(editorSource.getFile());
        } else {
            showNode = false;
            setLoadEditorText(editorSource.getFile());
            setGraph(GpxInformation.NULL, 0, -1, -1);
        }
    }


    @Override
    public GpxPointNode getSelectedNode() {
        return editorSource.getEditor().getSelected();
    }




    @Override
    public void setSelectedNode(int IID, GpxInformation info, GpxPointNode node, int index) {
        showNode = true;
        editorSource.getEditor().select(node);
        super.setSelectedNode(IID, info, node, index);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        super.onContentUpdated(iid, info);
        setBackgroundColorFromIID(iid);

        setText();
    }


    private void setLoadEditorText(Foc file) {
        html.appendHeader(file.getName());
        html.append(Res.str().edit_load());
        setHtmlText(html);
    }


    private void setNoNodeSelectedText(Foc file) {
        html.appendHeader(file.getName());
        setHtmlText(html);
    }

    @Override
    public void onClick(View v) {
        if (editorSource.isEditing())
            startNodeDetailActivity(ObjGpxEditable.getVirtualID(editorSource.getFile()));
        else
            editorSource.edit();

    }

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
