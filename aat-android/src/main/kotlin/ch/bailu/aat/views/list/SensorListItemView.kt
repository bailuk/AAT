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

    init {
        orientation = VERTICAL
        checkBox = CheckBox(context)
        checkBox.setTextColor(Color.LTGRAY)
        addView(checkBox)
        setItem(item)
        checkBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            this.item.setEnabled(isChecked)
            scontext.insideContext { scontext.sensorService.updateConnections() }
        }
        theme.content(checkBox)
    }

    fun setItem(item: SensorListItem) {
        this.item = item
        checkBox.isEnabled = this.item.isSupported
        checkBox.isChecked = this.item.isEnabled
        checkBox.text = this.item.toString()
    }
}
