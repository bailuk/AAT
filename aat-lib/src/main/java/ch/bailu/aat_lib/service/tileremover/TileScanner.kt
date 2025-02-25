package ch.bailu.aat_lib.service.tileremover

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.foc.Foc

abstract class TileScanner(private val root: Foc) {
    protected var source: String = ""
    protected var zoom: Short = 0
    protected var x = 0
    protected var y = 0
    protected var ext: String = ""

    companion object {
        fun doDirectory(file: Foc): Boolean {
            return file.isDir
        }
    }

    fun scanZoomContainer() {
        source = root.name
        scanZoomContainer(root)
    }

    fun scanSourceContainer() {
        scanSourceContainer(root)
    }

    private fun scanSourceContainer(dir: Foc) {
        if (doDirectory(dir) && doSourceContainer(dir)) {
            dir.foreachDir { child ->
                source = child.name
                scanZoomContainer(child)
            }
        }
    }

    protected abstract fun doSourceContainer(dir: Foc): Boolean
    private fun scanZoomContainer(dir: Foc) {
        if (doZoomContainer(dir)) {
            dir.foreachDir(object : Foc.OnHaveFoc {
                override fun run(child: Foc) {
                    try {
                        zoom = java.lang.Short.decode(child.name)
                        scanXContainer(child)
                    } catch (e: NumberFormatException) {
                        AppLog.w(this, e)
                    }
                }
            })
        }
    }

    protected abstract fun doZoomContainer(dir: Foc): Boolean
    private fun scanXContainer(dir: Foc) {
        if (doXContainer(dir)) {
            dir.foreachDir(object : Foc.OnHaveFoc {
                override fun run(child: Foc) {
                    try {
                        x = Integer.decode(child.name)
                        scanYContainer(child)
                    } catch (e: NumberFormatException) {
                        AppLog.w(this, e)
                    }
                }
            })
        }
    }

    protected abstract fun doXContainer(dir: Foc): Boolean
    private fun scanYContainer(dir: Foc) {
        if (doYContainer(dir)) {
            dir.foreachFile(object : Foc.OnHaveFoc {
                override fun run(child: Foc) {
                    try {
                        val parts = child.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        if (parts.size == 2) {
                            y = Integer.decode(parts[0])
                            ext = parts[1]
                            scanFile(child)
                        }
                    } catch (e: NumberFormatException) {
                        AppLog.w(this, e)
                    }
                }
            })
        }
    }

    protected abstract fun doYContainer(dir: Foc): Boolean
    private fun scanFile(file: Foc) {
        if (file.isFile) doFile(file)
    }

    protected abstract fun doFile(file: Foc)

}
