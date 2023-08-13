package ch.bailu.aat.app

import ch.bailu.aat_lib.util.WithStatusText

class AppThread : WithStatusText {
    override fun appendStatusText(builder: StringBuilder) {
        builder.append("<h1>").append(javaClass.simpleName).append("</h1>")
        builder.append("<p>")
        builder.append("<br><br>")

        val threads = arrayOfNulls<Thread>(Thread.activeCount() + 5)
        val count = Thread.enumerate(threads)

        for (i in 0 until count) {
            builder.append(threads[i]!!.id)
                .append(": ")
                .append(threads[i]!!.name)
                .append(", ")
                .append(threads[i]!!.state)
                .append("<br>")
        }
        builder.append("</p>")
    }
}
