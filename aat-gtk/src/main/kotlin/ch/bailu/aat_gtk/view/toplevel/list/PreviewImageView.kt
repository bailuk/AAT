package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.view.map.preview.MapsForgePreview
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjBitmap
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract
import ch.bailu.foc.Foc
import ch.bailu.gtk.gdk.Gdk
import ch.bailu.gtk.gtk.Image
import org.mapsforge.map.gtk.graphics.GtkBitmap

class PreviewImageView(private val appContext: AppContext): TargetInterface, Attachable {
    val image = Image().apply {
        pixelSize = MapsForgePreview.BITMAP_SIZE/2
    }


    private var idToLoad: String? = null
    private var factoryToLoad: Obj.Factory = Obj.Factory()
    private var handle: Obj = ObjNull

    fun setFilePath(fileID: Foc) {
        val file = appContext.summaryConfig.getPreviewFile(fileID)
        setPreviewPath(file)
    }

    private fun setPreviewPath(file: Foc) {
        setImageObject(file.path, ObjBitmap.Factory())
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setFilePath(info.getFile())
    }

    private fun setImageObject(id: String, factory: Obj.Factory) {
        idToLoad = id
        factoryToLoad = factory
        loadAndDisplayImage()
    }

    private fun loadAndDisplayImage() {
        val idToLoad = idToLoad

        if (idToLoad is String) {
            freeImageHandle()
            if (loadImage(idToLoad, factoryToLoad)) {
                displayImage()
            } else {
                resetImage()
            }
            this.idToLoad = null
            factoryToLoad = Obj.Factory()
        }
    }

    private fun freeImageHandle() {
        handle.free()
        handle = ObjNull
    }

    private fun loadImage(id: String, factory: Obj.Factory): Boolean {
        var result = false
        appContext.services.insideContext {
            val handle = appContext.services.getCacheService().getObject(id, factory)
            if (handle is ObjImageAbstract) {
                this.handle = handle
                result = true
            } else {
                handle.free()
            }
        }
        return result
    }

    private fun displayImage() {
        val imageHandle = handle
        if (imageHandle.hasException()) {
            resetImage()
        } else if (imageHandle is ObjImageAbstract && imageHandle.isReadyAndLoaded()) {
            val gtkBitmap = imageHandle.getBitmap()
            if (gtkBitmap is GtkBitmap) {
                val pixbuf = Gdk.pixbufGetFromSurface(gtkBitmap.surface, 0, 0, gtkBitmap.width, gtkBitmap.height)
                image.setFromPixbuf(pixbuf)
                pixbuf.unref()
            }
        }
    }

    private fun resetImage() {
        image.setFromFile("")
    }

    override fun onAttached() {
        appContext.broadcaster.register(
            AppBroadcaster.FILE_CHANGED_INCACHE,
            onFileChanged
        )
        loadAndDisplayImage()
    }

    override fun onDetached() {
        appContext.broadcaster.unregister(onFileChanged)
        freeImageHandle()
    }

    private val onFileChanged  = BroadcastReceiver {
        if (BroadcastData.getFile(it) == handle.toString()) {
            displayImage()
        }
    }
}
