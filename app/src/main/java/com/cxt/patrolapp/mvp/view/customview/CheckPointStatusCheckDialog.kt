package com.cxt.patrolapp.mvp.view.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.entity.GroupOfCheckPointAndDevice
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.dialog_check_point_status_check.view.*

class CheckPointStatusCheckDialog : DialogFragment() {

    var onRequestButtonClick: ((GroupOfCheckPointAndDevice) -> Unit)? = null

    private lateinit var group: GroupOfCheckPointAndDevice

    private val dialogTag = this.javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_check_point_status_check, container, false)
        view.close_button.setOnClickListener { dismiss() }
        refresh(view)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return view
    }

    fun show(group: GroupOfCheckPointAndDevice, manager: FragmentManager?) {
        this.group = group
        super.show(manager, dialogTag)
    }

    fun refresh(root: View? = null) {
        val rootView = root ?: view
        rootView?.let { view ->
            view.check_point_name_text.text = group.checkPoint.checkPointName
            view.request_button.setOnClickListener { onRequestButtonClick?.invoke(group) }
            val device = group.device
            when (device) {
                null -> {
                    val tip = view.context.getString(R.string.be_close_to_device_to_get_device_info)
                    view.device_name_text.setTextColor(ContextCompat.getColor(view.context, R.color.gray_999999))
                    view.device_status_text.setTextColor(ContextCompat.getColor(view.context, R.color.gray_999999))
                    view.device_name_text.text = tip
                    view.device_status_text.text = tip
                    view.request_button.visibility = View.GONE
                }
                else -> {
                    view.device_name_text.setTextColor(ContextCompat.getColor(view.context, R.color.gray_333333))
                    view.device_name_text.text = device.name
                    if (!device.isError) {
                        view.device_status_text.setTextColor(ContextCompat.getColor(view.context, R.color.gray_333333))
                        view.device_status_text.text = view.context.getString(R.string.device_status_is_normal)
                        view.request_button.visibility = View.GONE
                    } else {
                        view.device_status_text.setTextColor(ContextCompat.getColor(view.context, R.color.red_ff5555))
                        view.device_status_text.text = view.context.getString(R.string.device_status_low_power_full).with("${device.battery}%")
                        view.request_button.visibility = View.VISIBLE
                        when {
                            group.isRepairRequested -> {
                                view.request_button.isEnabled = false
                                view.request_button.text = getString(R.string.auto_requested_repair)
                            }
                            group.isRequestError -> {
                                view.request_button.isEnabled = true
                                view.request_button.text = getString(R.string.auto_re_request_repair)
                            }
                            else -> {
                                view.request_button.isEnabled = false
                                view.request_button.text = getString(R.string.auto_requesting_repair)
                            }
                        }
                    }
                }
            }
        }
    }
}