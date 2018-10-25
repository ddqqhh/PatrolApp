package com.cxt.patrolapp.mvp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.entity.GroupOfCheckPointAndDevice
import kotlinx.android.synthetic.main.item_check_point_status_check.view.*

class CheckPointStatusCheckView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var checkPointGroup: GroupOfCheckPointAndDevice

    init {
        inflate(context, R.layout.item_check_point_status_check, this)
    }

    fun setCheckPoint(group: GroupOfCheckPointAndDevice) {
        this.checkPointGroup = group
        check_point_name.text = group.checkPoint.checkPointName

        val device = group.device

        if (group.checkPoint.status == CheckPointStatus.REPAIR_REQUESTING) {
            progress_bar.visibility = View.GONE
            device_status_text.visibility = View.GONE
            device_status_icon.visibility = View.VISIBLE
            repair_status_text.visibility = View.VISIBLE
            repair_status_text.text = context.getString(R.string.repairing)
            device_status_icon.setBackgroundResource(R.drawable.device_status_reqairing)
        } else {
            if (device == null) {
                progress_bar.visibility = View.VISIBLE
                device_status_text.visibility = View.GONE
                device_status_icon.visibility = View.GONE
                repair_status_text.visibility = View.GONE
            } else {
                progress_bar.visibility = View.GONE
                device_status_icon.visibility = View.VISIBLE
                if (!device.isError) {
                    device_status_text.visibility = View.GONE
                    repair_status_text.visibility = View.GONE
                    device_status_icon.setBackgroundResource(R.drawable.device_status_ok)
                } else {
                    device_status_text.visibility = View.VISIBLE
                    repair_status_text.visibility = View.VISIBLE
                    device_status_text.text = context.getString(R.string.device_status_low_power_full).with("${device.battery}%")
                    device_status_icon.setBackgroundResource(R.drawable.device_status_error)

                    when {
                        group.isRepairRequested -> repair_status_text.text = context.getString(R.string.auto_requested_repair)
                        !group.isRepairRequested && !group.isRequestError -> repair_status_text.text = context.getString(R.string.auto_requested_repair)
                        !group.isRepairRequested && group.isRequestError -> repair_status_text.text = context.getString(R.string.auto_request_repair_error)
                    }
                }
            }
        }
    }
}