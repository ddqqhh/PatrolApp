package com.cxt.patrolapp.mvp.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.showToast
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.extend.startActivityForResult
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.presenter.DeviceManagePresenter
import com.cxt.patrolapp.mvp.view.contract.DeviceManageView
import kotlinx.android.synthetic.main.activity_device_manage.*

class DeviceManageActivity : BaseActivity(), DeviceManageView {

    private val presenter = DeviceManagePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_manage)

        bind_new_one_button.setOnClickListener {
            startActivity(DeviceScanActivity::class.java)
        }
        repair_button.setOnClickListener {
            startActivityForResult(CheckPointListActivity::class.java, CommonConst.GET_CHECK_POINT_FOR_REPAIR_REQUEST)
        }
        rebind_button.setOnClickListener {
            startActivityForResult(CheckPointListActivity::class.java, CommonConst.GET_CHECK_POINT_FOR_REPLACE_REQUEST)
        }
        logout_button.setOnClickListener {
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_logout))
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> presenter.logout() }
                    .show()
        }

        close_button.setOnClickListener { finish() }
    }

    override fun repairRequestSuccess() {
        showToast(getString(R.string.repair_request_success))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CommonConst.GET_CHECK_POINT_FOR_REPAIR_REQUEST && resultCode == CommonConst.GET_CHECK_POINT_RESULT) {
            val checkPoint = data?.getSerializableExtra(CommonConst.CHECK_POINT) as? CheckPoint
            checkPoint?.let {
                val tip = Html.fromHtml(getString(R.string.alert_repair_request_confirm).with(it.checkPointName))
                AlertDialog.Builder(this)
                        .setMessage(tip)
                        .setPositiveButton(getString(R.string.ok)) { _, _ -> presenter.repairRequest(it) }
                        .setNeutralButton(getString(R.string.cancel), null)
                        .show()
            }
        } else if (requestCode == CommonConst.GET_CHECK_POINT_FOR_REPLACE_REQUEST && resultCode == CommonConst.GET_CHECK_POINT_RESULT) {
            val checkPoint = data?.getSerializableExtra(CommonConst.CHECK_POINT) as? CheckPoint
            startActivity(DeviceScanActivity::class.java) {
                it.putExtra(CommonConst.CHECK_POINT, checkPoint)
            }
        }
    }
}