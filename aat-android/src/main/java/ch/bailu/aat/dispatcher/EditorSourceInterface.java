package ch.bailu.aat.dispatcher;

import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.foc.Foc;

public interface EditorSourceInterface {
    boolean isEditing();
    EditorInterface getEditor();
    Foc getFile();

    void edit();
}
