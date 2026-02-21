package ch.bailu.aat_lib.service.editor

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EditorRingTest {

    @Test
    fun testEditorRing() {
        val editor = NodeEditor()
        val editor2 = NodeEditor()
        val ring = EditorRing(editor)

        assertEquals(editor, ring.get())

        ring.add(editor2)
        assertEquals(editor2, ring.get())

        ring.undo()
        assertEquals(editor, ring.get())

        ring.undo()
        assertEquals(editor, ring.get())

        ring.redo()
        assertEquals(editor2, ring.get())

        ring.redo()
        assertEquals(editor2, ring.get())
    }
}
