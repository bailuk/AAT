package ch.bailu.aat.views.list

import android.graphics.Color
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.list.SensorListItem
import ch.bailu.aat.util.ui.theme.UiTheme

class SensorListItemView(private val scontext: ServiceContext, private var item: SensorListItem, theme: UiTheme) :
    LinearLayout(scontext.getContext()) {

    private val checkBox: CheckBox

    /**
     * Inhibit any updates to the #SensorListItem in UI callbacks?
     */
    private var inhibitItemUpdates = false

    init {
        orientation = VERTICAL
        checkBox = CheckBox(context)
        checkBox.setTextColor(Color.LTGRAY)
        addView(checkBox)
        setItem(item)
        checkBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (inhibitItemUpdates)
                return@setOnCheckedChangeListener
            this.item.setEnabled(isChecked)
            scontext.insideContext { scontext.getSensorService().updateConnections() }
        }
        theme.content(checkBox)
    }

    fun setItem(item: SensorListItem) {
        inhibitItemUpdates = true
        this.item = item
        checkBox.isEnabled = this.item.isSupported
        checkBox.isChecked = this.item.isEnabled
        checkBox.text = this.item.toString()
        inhibitItemUpdates = false
    }
}
