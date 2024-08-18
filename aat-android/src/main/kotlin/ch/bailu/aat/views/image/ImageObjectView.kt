package ch.bailu.aat.views.image

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.map.To.androidBitmap
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract

open class ImageObjectView(
    private val scontext: ServiceContext,
    private val defaultImageID: Int
) : ImageView(
    scontext.getContext()
) {
    private var isAttached = false
    private var handle = ObjNull.NULL
    private var idToLoad: String? = null
    private var factoryToLoad: Obj.Factory? = null
    fun setImageObject() {
        idToLoad = null
        factoryToLoad = null
        resetImage()
    }

    fun setImageObject(ID: String?, factory: Obj.Factory?) {
        idToLoad = ID
        factoryToLoad = factory
        loadAndDisplayImage()
    }

    private fun loadAndDisplayImage() {
        val idToLoad = idToLoad

        if (idToLoad != null && isAttached) {
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

    private fun resetImage() {
        if (defaultImageID != 0) setImageResource(defaultImageID) else setImageDrawable(null)
    }

    private fun loadImage(id: String, factory: Obj.Factory?): Boolean {
        var result = false
        scontext.insideContext {
            val h = scontext.cacheService.getObject(id, factory)
            if (h is ObjImageAbstract) {
                handle = h
                result = true
            } else {
                h.free()
            }
        }
        return result
    }

    private fun freeImageHandle() {
        handle.free()
        handle = ObjNull.NULL
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isAttached = true
        AndroidBroadcaster.register(
            context,
            onFileChanged,
            AppBroadcaster.FILE_CHANGED_INCACHE
        )
        loadAndDisplayImage()
    }

    public override fun onDetachedFromWindow() {
        context.unregisterReceiver(onFileChanged)
        freeImageHandle()
        isAttached = false
        super.onDetachedFromWindow()
    }

    private fun displayImage() {
        val imageHandle = handle

        if (imageHandle.hasException()) {
            resetImage()
        } else if (imageHandle is ObjImageAbstract && imageHandle.isReadyAndLoaded) {
            setImageBitmap(androidBitmap(imageHandle.bitmap))
        }
    }

    private val onFileChanged: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val file = handle.toString()
            if (AppIntent.hasFile(intent, file)) {
                displayImage()
            }
        }
    }

    init {
        resetImage()
    }
}
