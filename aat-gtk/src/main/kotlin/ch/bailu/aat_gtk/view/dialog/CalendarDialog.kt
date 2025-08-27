package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.Dialog
import ch.bailu.gtk.glib.DateTime
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Calendar
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


object CalendarDialog {
    private var isActive = false

    /**
     * Displays a modal date picker dialog and
     * returns a Unix timestamp via callback if
     * the user has selected 'OK'
     */
    fun getDate(parent: Widget, timestamp: Long, onHaveDate: (timestamp: Long) -> Unit) {
        if (!isActive) {
            val calendar = Calendar().apply {
                val localDate = toLocalDate(timestamp)
                day = 1
                year = localDate.year
                month = localDate.monthValue
                day = localDate.dayOfMonth
            }

            val dialog = Dialog()
            val buttonCancel = Button().apply {
                setLabel(Res.str().cancel())
                onClicked { dialog.close() }
            }

            val buttonOK = Button().apply {
                setLabel(Res.str().ok())
                onClicked {
                    onHaveDate(toUnixTimestamp(calendar.date))
                    dialog.close()
                }
            }

            //dialog.setTitle("Datum Wählen")

            dialog.child = Box(Orientation.VERTICAL, Layout.MARGIN).apply {
                margin(Layout.MARGIN*2)
                append(calendar)
                append(Box(Orientation.HORIZONTAL, Layout.MARGIN).apply {
                    append(buttonCancel)
                    append(Label(Str.NULL).apply {
                        hexpand =true
                    })
                    append(buttonOK)
                })
            }
            dialog.onDestroy {
                dialog.disconnectSignals()
                calendar.disconnectSignals()
                dialog.disconnectSignals()
                buttonOK.disconnectSignals()
                buttonCancel.disconnectSignals()
                isActive = false
            }
            dialog.present(parent)
            isActive = true
        }
    }

    private fun toLocalDate(unixTimestamp: Long): LocalDate {
        return Instant.ofEpochMilli(unixTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun toUnixTimestamp(date: DateTime): Long {
        return date.toUnix()*1000
    }
}
