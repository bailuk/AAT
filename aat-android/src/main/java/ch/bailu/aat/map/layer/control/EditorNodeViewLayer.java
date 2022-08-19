package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.view.View;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.cache.ObjGpxEditable;
import ch.bailu.foc.Foc;

public final class EditorNodeViewLayer extends NodeViewLayer {

    private final EditorSourceInterface editorSource;
    private boolean showNode = false;

    public EditorNodeViewLayer(AppContext appContext, Context context, MapContext mc, EditorSourceInterface e) {
        super(appContext, context, mc);
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
    public void setSelectedNode(int IID, @Nonnull GpxInformation info, @Nonnull GpxPointNode node, int index) {
        showNode = true;
        editorSource.getEditor().select(node);
        super.setSelectedNode(IID, info, node, index);
    }


    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        super.onContentUpdated(iid, info);
        setBackgroundColorFromIID(iid);

        setText();
    }


    private void setLoadEditorText(Foc file) {
        markupBuilder.appendHeaderNl(file.getName());
        markupBuilder.append(Res.str().edit_load());
        setHtmlText(markupBuilder);
    }


    private void setNoNodeSelectedText(Foc file) {
        markupBuilder.appendHeaderNl(file.getName());
        setHtmlText(markupBuilder);
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
