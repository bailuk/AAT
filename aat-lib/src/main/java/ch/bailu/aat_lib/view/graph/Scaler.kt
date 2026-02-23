package ch.bailu.aat_lib.view.graph

class Scaler(scale: Float = 1f, real: Float = 1f) {
    val scale: Float
    var real: Float
        private set

    init {
        if (scale != 0f) {
            this.scale = scale
        } else {
            this.scale = 1f
        }

        if (real != 0f) {
            this.real = real
        } else {
            this.real = 1f
        }
    }

    fun init(real: Float) {
        if (real != 0f) {
            this.real = real
        }
    }

    fun scale(realValue: Float): Float {
        return (scale / real) * realValue
    }

    fun backScale(scaledValue: Float): Float {
        return scaledValue / (scale / real)
    }
}
