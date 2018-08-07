package com.cxt.patrolapp.mvp.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.View
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import kotlinx.android.synthetic.main.activity_check_point_list.*
import kotlinx.android.synthetic.main.item_patrol_schedule_progress.view.*
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.presenter.PatrolScheduleProgressPresenter
import com.cxt.patrolapp.mvp.view.contract.PatrolScheduleProgressView


class PatrolScheduleProgressActivity : BaseActivity(), PatrolScheduleProgressView {

    private val presenter = PatrolScheduleProgressPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_point_list)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_arrow, null)
        toolbar.setNavigationOnClickListener { finish() }

        @Suppress("UNCHECKED_CAST")
        val checkPointList = intent.getSerializableExtra(CommonConst.CHECK_POINT) as? List<CheckPoint>

        if (checkPointList?.isNotEmpty() == true) {
            scroll_view.visibility = View.VISIBLE
            no_result_icon.visibility = View.GONE

            val repairRequestListener: (CheckPoint) -> Unit = {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.alert_check_point_is_trouble))
                        .setPositiveButton(getString(R.string.ok)) { _, _ ->
                            val tip = Html.fromHtml(getString(R.string.alert_repair_request_confirm).with(it.checkPointName))
                            AlertDialog.Builder(this)
                                    .setMessage(tip)
                                    .setPositiveButton(getString(R.string.ok)) { _, _ -> presenter.repairRequest(it) }
                                    .setNeutralButton(getString(R.string.cancel), null)
                                    .show()
                        }
                        .show()
            }

            checkPointList.forEach {
                val checkPointView = layoutInflater.inflate(R.layout.item_patrol_schedule_progress, check_point_list_view, false)
                checkPointView.check_point_text.text = it.checkPointName
                when (it.isChecked) {
                    true -> {
                        checkPointView.status_icon.setBackgroundResource(R.drawable.shape_check_point_be_checked_background)
                        checkPointView.status_text.text = getString(R.string.be_checked)
                    }
                    false -> {
                        checkPointView.status_icon.setBackgroundResource(R.drawable.shape_check_point_not_be_checked_background)
                        checkPointView.status_text.text = getString(R.string.not_be_check)
                    }
                }
                when (it.status) {
                    CheckPointStatus.NORMAL -> {
                        checkPointView.device_status.visibility = View.GONE
//                        checkPointView.setOnLongClickListener { _ ->
//                            repairRequestListener.invoke(it)
//                            true
//                        }
                    }
                    CheckPointStatus.REPAIR_REQUESTING -> checkPointView.device_status.visibility = View.VISIBLE
                    CheckPointStatus.UNKNOWN -> Unit
                }
                check_point_list_view.addView(checkPointView)
            }
        } else {
            scroll_view.visibility = View.GONE
            no_result_icon.visibility = View.VISIBLE
        }
    }

    override fun repairRequestSuccess() {
    }
}