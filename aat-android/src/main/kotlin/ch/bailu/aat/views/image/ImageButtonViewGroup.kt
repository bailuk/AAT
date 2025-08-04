package ch.bailu.aat.views.image

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat.util.ui.theme.AppTheme.padding

open class ImageButtonViewGroup(context: Context, image_res: Int) : FrameLayout(context) {
    private val imageView: ImageView = ImageView(context)

    init {
        imageView.setImageResource(image_res)
        imageView.isClickable = false
        addView(imageView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    fun setImageResource(image_res: Int) {
        imageView.setImageResource(image_res)
    }

    public override fun onMeasure(wSpec: Int, hSpec: Int) {
        setMinPadding(MeasureSpec.getSize(wSpec), MeasureSpec.getSize(hSpec))
        super.onMeasure(wSpec, hSpec)
    }

    private fun setMinPadding(width: Int, height: Int) {
        val pixSize = Math.min(width, height)
        val size = AndroidAppDensity(context).toDensityIndependentPixel(pixSize.toFloat())
        val padding = Math.max((size - MAX_IMAGE_SIZE) / 2, 0)
        padding(this, padding)
    }

    companion object {
        private const val MAX_IMAGE_SIZE = 40
    }
}
