package ch.bailu.aat_lib.exception

import java.lang.IllegalArgumentException

class IllegalCodeException(code: String) : IllegalArgumentException("The provided code '$code' is not a valid.")
