package com.cxt.patrolapp.mvp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.with
import com.skybeacon.sdk.locate.SKYBeacon
import kotlinx.android.synthetic.main.view_device_info.view.*
import java.util.*

class DeviceInfoItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    var device: SKYBeacon? = null
        set(value) {
            field = value
            if (value != null) {
                val snCode = context.getString(R.string.sn_format).with(value.deviceAddress.replace(":", ""))
                device_sn_code.text = snCode
                val distanceString = String.format(Locale.getDefault(), "%.2f", value.distance)
                device_distance.text = context.getString(R.string.distance_format).with(distanceString)
            }
        }

    init {
        inflate(context, R.layout.view_device_info, this)
    }
}