package ch.bailu.aat.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.map.To.androidBitmap
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.cache.ObjBitmap
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract

open class ImageObjectView(
    private val scontext: ServiceContext,
    private val defaultImageID: Int
) : ImageView(
    scontext.getContext()
) {
    private var isAttached = false
    private var imageHandle: ObjImageAbstract = ObjBitmap.NULL
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
        if (idToLoad != null && isAttached) {
            freeImageHandle()
            if (loadImage(idToLoad!!, factoryToLoad)) {
                displayImage()
            } else {
                resetImage()
            }
            idToLoad = null
            factoryToLoad = null
        }
    }

    private fun resetImage() {
        if (defaultImageID != 0) setImageResource(defaultImageID) else setImageDrawable(null)
    }

    private fun loadImage(id: String, factory: Obj.Factory?): Boolean {
        val r = booleanArrayOf(false)
        scontext.insideContext {
            val h = scontext.cacheService.getObject(id, factory)
            if (h is ObjImageAbstract) {
                imageHandle = h
                r[0] = true
            } else {
                h.free()
            }
        }
        return r[0]
    }

    private fun freeImageHandle() {
        imageHandle.free()
        imageHandle = ObjBitmap.NULL
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
        if (imageHandle.hasException()) {
            resetImage()
        } else if (imageHandle.isReadyAndLoaded) {
            setImageBitmap(androidBitmap(imageHandle.bitmap))
        }
    }

    private val onFileChanged: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val file = imageHandle.toString()
            if (AppIntent.hasFile(intent, file)) displayImage()
        }
    }

    init {
        resetImage()
    }
}
