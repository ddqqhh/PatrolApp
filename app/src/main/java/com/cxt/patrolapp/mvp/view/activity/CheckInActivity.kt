package com.cxt.patrolapp.mvp.view.activity

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.contract.CheckInView
import com.cxt.patrolapp.utils.SkyBeaconManager
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.model.entity.PatrolSchedule
import com.cxt.patrolapp.mvp.presenter.CheckInPresenter
import com.cxt.patrolapp.utils.VibratorManager
import kotlinx.android.synthetic.main.activity_check_in.*
import java.io.Serializable
import java.text.DecimalFormat

class CheckInActivity : BaseActivity(), CheckInView {

    private lateinit var patrolPlan: PatrolSchedule

    private var currentCheckPoint: CheckPoint? = null
    private var checkPointList = listOf<CheckPoint>()

    private val presenter = CheckInPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        val patrolPlan = intent.getSerializableExtra(CommonConst.PATROL_PLAN) as? PatrolSchedule

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_arrow_white, null)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { finish() }

        val showProgressListener: (List<CheckPoint>) -> Unit = { pointList ->
            startActivity(PatrolScheduleProgressActivity::class.java) { it.putExtra(CommonConst.CHECK_POINT, pointList as Serializable) }
        }
        checked_count_text.text = getString(R.string.checked_count_format).with("00")
        uncheck_count_text.text = getString(R.string.uncheck_count_format).with("00")
        checked_count_text.setOnClickListener { showProgressListener.invoke(checkPointList) }
        uncheck_count_text.setOnClickListener { showProgressListener.invoke(checkPointList) }

        check_in_button.setListener(onTriggered = {
            checkPointList.firstOrNull { it.deviceAddress == currentCheckPoint?.deviceAddress }
                    ?.let {
                        VibratorManager.vibrate(300)
                        presenter.checkIn(it)
                    }
        })

        patrolPlan?.let {
            if (!it.status.isDone()) {
                this.patrolPlan = it
                presenter.loadCheckPoint(it)
            } else {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.alert_all_check_point_is_checked))
                        .setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
                        .setCancelable(false)
                        .show()
            }
        }
    }

    override fun onLoadCheckPointSuccess(pointList: List<CheckPoint>) {
        checkPointList = pointList
        if (checkPointList.isNotEmpty()) {
            val doneSum = checkPointList.filter { it.isChecked }.size
            val doingSum = checkPointList.size - doneSum
            val format = DecimalFormat("00")
            checked_count_text.text = getString(R.string.checked_count_format).with(format.format(doneSum))
            uncheck_count_text.text = getString(R.string.uncheck_count_format).with(format.format(doingSum))
            if (doingSum == 0) {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.alert_all_check_point_is_checked))
                        .setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
                        .setCancelable(false)
                        .show()
            } else {
                SkyBeaconManager.startRange { deviceList ->
                    currentCheckPoint = deviceList.filter { it.distance < 0.2 }
                            .sortedBy { it.distance }
                            .firstOrNull()
                            ?.let { device -> checkPointList.firstOrNull { it.deviceAddress == device.deviceAddress } }
                    runOnUiThread { refreshView() }
                }
            }

        } else {
            checked_count_text.text = getString(R.string.checked_count_format).with("00")
            uncheck_count_text.text = getString(R.string.uncheck_count_format).with("00")
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_check_point_is_empty))
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
                    .setCancelable(false)
                    .show()
        }
    }

    override fun onCheckInSuccess() {
        val reloadAction = {
            currentCheckPoint = null
            presenter.loadCheckPoint(patrolPlan)
        }
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.check_in_success))
                .setPositiveButton(getString(R.string.ok)) { _, _ -> reloadAction.invoke() }
                .setOnCancelListener { reloadAction.invoke() }
                .show()
    }

    private fun refreshView() {
        if (currentCheckPoint == null) {
            check_in_button.isEnabled = false
            check_in_button.visibility = View.GONE
            check_point_text.visibility = View.GONE
            searching_tip.visibility = View.VISIBLE
        } else {
            when (currentCheckPoint?.isChecked) {
                false -> {
                    check_in_button.isEnabled = true
                    check_in_button.buttonTitle = getString(R.string.check_in)
                }
                true -> {
                    check_in_button.isEnabled = false
                    check_in_button.buttonTitle = getString(R.string.be_checked)
                }
            }
            check_in_button.visibility = View.VISIBLE
            check_point_text.visibility = View.VISIBLE
            check_point_text.text = currentCheckPoint?.checkPointName
            searching_tip.visibility = View.GONE
        }
    }
}