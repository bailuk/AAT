package ch.bailu.aat_lib.logger

object Validator {
    fun validateNotNull(obj: Any?, reason: String): Boolean {
        if (obj == null) {
            AppLog.e("Validator", reason)
            return false
        }
        return true
    }
}
