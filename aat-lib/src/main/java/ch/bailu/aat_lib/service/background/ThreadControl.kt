package ch.bailu.aat_lib.service.background

interface ThreadControl {
    fun canContinue(): Boolean

    companion object {
        val KEEP_ON: ThreadControl = object: ThreadControl {
            override fun canContinue(): Boolean {
                return true
            }
        }
    }
}
