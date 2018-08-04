package com.cxt.patrolapp.mvp.view.activity

import android.os.Bundle
import android.view.View
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.customview.DeviceInfoItem
import com.cxt.keepnightwatch.utils.SkyBeaconManager
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.startActivity
import com.skybeacon.sdk.locate.SKYBeacon
import kotlinx.android.synthetic.main.activity_device_scan.*

class DeviceScanActivity : BaseActivity() {

    private var currentDevice: SKYBeacon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_scan)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_arrow, null)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.inflateMenu(R.menu.refresh_menu)
        toolbar.isOverflowMenuShowing
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh_button -> {
                    SkyBeaconManager.stop()
                    startScan()
                }
                else -> Unit
            }
            true
        }

        device_info_view.setOnClickListener {
            val deviceView = it as DeviceInfoItem
            startActivity(CheckPointBindActivity::class.java) {
                it.putExtra(CommonConst.DEVICE, deviceView.device)
                it.putExtra(CommonConst.CHECK_POINT, intent.getSerializableExtra(CommonConst.CHECK_POINT))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startScan()
        progress_bar.visibility = View.VISIBLE
        device_info_view.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        SkyBeaconManager.stop()
    }

    private fun startScan() {
        progress_bar.visibility = View.VISIBLE
        device_info_view.visibility = View.GONE
        SkyBeaconManager.startMonitor { deviceList ->
            runOnUiThread {
                deviceList.sortedBy { it.distance }
                        .firstOrNull()
                        ?.let {
                            progress_bar.visibility = View.GONE
                            device_info_view.visibility = View.VISIBLE
                            device_info_view.device = it
                            currentDevice = it
                            SkyBeaconManager.stop()
                        }
            }
        }
    }
}