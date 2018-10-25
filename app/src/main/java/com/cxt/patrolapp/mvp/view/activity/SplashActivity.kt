package com.cxt.patrolapp.mvp.view.activity

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.cxt.patrolapp.R
import com.cxt.patrolapp.mvp.view.contract.SplashView
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.mvp.presenter.SplashPresenter

class SplashActivity : BaseActivity(), SplashView {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val presenter = SplashPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (bluetoothAdapter?.enable() == true) {
            checkLocationPermission { presenter.checkUid() }
        } else {
            AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.alert_bluetooth_is_unavailable)
                    .setPositiveButton(R.string.ok) { _, _ -> finish() }
                    .show()
        }
    }

    override fun moveToMain() {
        startActivity(MainActivity::class.java)
        finish()
    }
}