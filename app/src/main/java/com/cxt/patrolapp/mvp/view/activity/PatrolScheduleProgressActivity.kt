package com.cxt.patrolapp.mvp.view.activity

import android.os.Bundle
import android.view.View
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import kotlinx.android.synthetic.main.activity_check_point_list.*
import kotlinx.android.synthetic.main.item_patrol_schedule_progress.view.*

class PatrolScheduleProgressActivity : BaseActivity() {

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
                    CheckPointStatus.NORMAL -> checkPointView.device_status.visibility = View.GONE
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
}