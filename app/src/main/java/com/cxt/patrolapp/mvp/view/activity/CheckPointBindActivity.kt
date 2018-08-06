package com.cxt.patrolapp.mvp.view.activity

import android.os.Bundle
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.showToast
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.presenter.CheckPointBindPresenter
import com.cxt.patrolapp.mvp.view.contract.CheckPointBindView
import com.skybeacon.sdk.locate.SKYBeacon
import kotlinx.android.synthetic.main.activity_check_point_bind.*

class CheckPointBindActivity : BaseEditTextActivity(), CheckPointBindView {

    private val MODE_ADD = 0
    private val MODE_REPLACE = 1

    private var mode = MODE_ADD

    private val presenter = CheckPointBindPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_point_bind)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_arrow, null)
        toolbar.setNavigationOnClickListener { finish() }

        val checkPoint = intent.getSerializableExtra(CommonConst.CHECK_POINT) as? CheckPoint
        checkPoint?.let {
            check_point_edit.setText(checkPoint.checkPointName)
            check_point_edit.isEnabled = false
        }

        val toolbarTitle = when (checkPoint == null) {
            true -> getString(R.string.add_check_point)
            false -> getString(R.string.device_replace)
        }
        toolbar.title = toolbarTitle

        val device = intent.getSerializableExtra(CommonConst.DEVICE) as? SKYBeacon
        device?.let {
            val snCode = getString(R.string.sn_format).with(it.deviceAddress.replace(":", ""))
            sn_code_text.text = snCode
            submit_button.setOnClickListener {
                when (checkPoint != null) {
                    true -> presenter.replaceCheckPoint(device.deviceAddress, checkPoint!!)
                    false -> presenter.bindCheckPoint(device.deviceAddress, check_point_edit.text.toString())
                }
            }
        }
    }

    override fun onBindSuccess() {
        showToast(getString(R.string.check_point_bind_success))
        startActivity(MainActivity::class.java)
    }
}