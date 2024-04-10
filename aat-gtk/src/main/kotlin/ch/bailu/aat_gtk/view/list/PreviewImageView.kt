package ch.bailu.aat_gtk.view.list

import ch.bailu.aat_gtk.view.map.preview.MapsForgePreview
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.BroadcastData
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjBitmap
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract
import ch.bailu.foc.Foc
import ch.bailu.gtk.gdk.Gdk
import ch.bailu.gtk.gtk.Image
import org.mapsforge.map.gtk.graphics.GtkBitmap

class PreviewImageView(private val appContext: AppContext): OnContentUpdatedInterface, Attachable {
    val image = Image().apply {
        pixelSize = MapsForgePreview.BITMAP_SIZE/2
    }


    private var idToLoad: String? = null
    private var factoryToLoad: Obj.Factory? = null
    private var handle: Obj = ObjNull.NULL

    fun setFilePath(fileID: Foc) {
        val file = appContext.summaryConfig.getPreviewFile(fileID)
        setPreviewPath(file)
    }

    private fun setPreviewPath(file: Foc) {
        setImageObject(file.path, ObjBitmap.Factory())
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setFilePath(info.file)
    }

    private fun setImageObject(ID: String?, factory: Obj.Factory?) {
        idToLoad = ID
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
            factoryToLoad = null
        }
    }

    private fun freeImageHandle() {
        handle.free()
        handle = ObjNull.NULL
    }

    private fun loadImage(id: String, factory: Obj.Factory?): Boolean {
        var result = false
        appContext.services.insideContext {
            val handle = appContext.services.cacheService.getObject(id, factory)
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
        } else if (imageHandle is ObjImageAbstract && imageHandle.isReadyAndLoaded) {
            val gtkBitmap = imageHandle.bitmap
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
