package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.service.editor.EditorInterface;
import ch.bailu.foc.Foc;

public interface EditorSourceInterface {
    boolean isEditing();
    EditorInterface getEditor();
    Foc getFile();

    void edit();
}
