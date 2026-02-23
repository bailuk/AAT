package ch.bailu.aat_gtk.lib

import ch.bailu.aat_lib.util.MemSize
import ch.bailu.gtk.lib.util.SizeLog

class RuntimeInfo : Runnable {
    override fun run() {
        val max = SizeLog(getIdentifier("Max"))
        val total = SizeLog(getIdentifier("Total"))
        val free = SizeLog(getIdentifier("Free"))
        val used = SizeLog(getIdentifier("Used"))
        val processors = SizeLog(getIdentifier("Processors"))

        val runtime = Runtime.getRuntime()

        try {
            Thread.sleep(TIMEOUT)
            processors.log(runtime.availableProcessors().toLong())
            free.log(runtime.freeMemory() / MemSize.MB)

            while (on) {
                max.log(runtime.maxMemory() / MemSize.MB)
                total.log(runtime.totalMemory() / MemSize.MB)
                used.log((runtime.totalMemory() - runtime.freeMemory()) / MemSize.MB)
                Thread.sleep(TIMEOUT)
            }
        } catch (e: InterruptedException) {
            on = false
            System.err.println(e.message)
        }
    }

    companion object {
        private const val TIMEOUT: Long = 5000
        private var on = false

        private fun getIdentifier(name: String): String {
            return Runtime::class.java.simpleName + ":" + name
        }

        @Synchronized
        fun startLogging() {
            if (!on) {
                on = true
                Thread(RuntimeInfo()).start()
            }
        }

        @Synchronized
        fun stopLogging() {
            on = false
        }
    }
}
