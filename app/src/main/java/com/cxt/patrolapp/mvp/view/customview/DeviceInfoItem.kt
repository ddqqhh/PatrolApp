package com.cxt.patrolapp.mvp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.entity.BeaconDevice
import kotlinx.android.synthetic.main.view_device_info.view.*
import java.util.*

class DeviceInfoItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var device: BeaconDevice

    init {
        inflate(context, R.layout.view_device_info, this)
    }

    fun createView(device: BeaconDevice) {
        this.device = device
        device_name_text.text = device.name
        val distanceString = String.format(Locale.getDefault(), "%.2f", device.distance)
        device_distance_text.text = context.getString(R.string.distance_format_simple).with(distanceString)
        when (device.isError) {
            false -> {
                device_status_view.isActivated = false
                device_status_view.text = context.getString(R.string.device_status_is_normal)
            }
            true -> {
                device_status_view.isActivated = true
                device_status_view.text = context.getString(R.string.device_status_low_power).with("${device.battery}%")
            }
        }
    }
}