package ch.bailu.aat.map.layer

import android.content.Context
import android.view.View
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxEditable
import ch.bailu.foc.Foc

class EditorNodeViewLayer(appContext: AppContext,
                          context: Context,
                          mcontext: MapContext,
                          private val editorSource: EditorSourceInterface
) : NodeViewLayer(appContext, context, mcontext) {

    private var showNode = false

    init {
        setText()
    }

    private fun setText() {
        if (editorSource.isEditing) {
            if (!showNode) setNoNodeSelectedText(editorSource.file)
        } else {
            showNode = false
            setLoadEditorText(editorSource.file)
            setGraph(GpxInformation.NULL, 0, -1, -1)
        }
    }

    override fun getSelectedNode(): GpxPointNode? {
        return editorSource.editor.selected
    }

    override fun setSelectedNode(
        iid: Int,
        info: GpxInformation,
        node: GpxPointNode,
        index: Int
    ) {
        showNode = true
        editorSource.editor.select(node)
        super.setSelectedNode(iid, info, node, index)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        super.onContentUpdated(iid, info)
        setBackgroundColorFromIID(iid)
        setText()
    }

    private fun setLoadEditorText(file: Foc) {
        markupBuilder.appendHeader(file.name)
        markupBuilder.append(Res.str().edit_load())
        setHtmlText(markupBuilder)
    }

    private fun setNoNodeSelectedText(file: Foc) {
        markupBuilder.appendHeader(file.name)
        setHtmlText(markupBuilder)
    }

    override fun onClick(v: View) {
        if (editorSource.isEditing) {
            startNodeDetailActivity(ObjGpxEditable.getVirtualID(editorSource.file))
        } else {
            editorSource.edit()
        }
    }

    override fun onAttached() {}
    override fun onDetached() {}
}
