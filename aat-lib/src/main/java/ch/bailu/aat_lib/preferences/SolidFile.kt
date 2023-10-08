package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import javax.annotation.Nonnull

abstract class SolidFile(storage: StorageInterface, key: String, private val focFactory: FocFactory) : SolidString(storage, key), SolidFileInterface {
    override fun getValueAsFile(): Foc {
        return focFactory.toFoc(getValueAsString())
    }

    @Nonnull
    override fun toString(): String {
        return getValueAsFile().pathName
    }

    override fun getIconResource(): String {
        return "folder_inverse"
    }

    abstract override fun buildSelection(list: ArrayList<String>): ArrayList<String>

    override fun getToolTip(): String {
        return getPermissionText(getValueAsFile())
    }

    companion object {
        fun getPermissionText(file: Foc): String {
            return if (!file.exists()) {
                if (file.hasParent()) {
                    getPermissionText(file.parent())
                } else {
                    file.pathName + ": " + Res.str().file_is_missing()
                }
            } else if (file.canWrite()) {
                if (file.canRead()) {
                    file.pathName + ": " + Res.str().file_is_writeable()
                } else {
                    file.pathName + ": " + Res.str().file_is_writeonly()
                }
            } else if (file.canRead()) {
                file.pathName + ": " + Res.str().file_is_readonly()
            } else if (file.hasParent()) {
                getPermissionText(file.parent())
            } else {
                file.pathName + ": " + Res.str().file_is_denied()
            }
        }

        fun addByExtensionIncludeSubdirectories(list: ArrayList<String>, directory: Foc, ext: String): ArrayList<String> {
            directory.foreachDir { child: Foc -> addByExtension(list, child, ext) }
            return list
        }

        fun addByExtension(list: ArrayList<String>, directory: Foc, ext: String): ArrayList<String> {
            directory.foreachFile { child: Foc ->
                if (child.name.endsWith(ext)) SelectionList.add_r(
                    list,
                    child
                )
            }
            return list
        }
    }
}
