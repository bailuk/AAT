package ch.bailu.aat_lib.logger

import java.io.PrintStream

class PrintLnLogger(private val prefix: String, private val target: PrintStream = System.out) : Logger {
    override fun log(tag: String, msg: String) {
        target.println("[$prefix] $tag: $msg")
    }
}
