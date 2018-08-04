package com.cxt.patrolapp.mvp.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import com.cxt.patrolapp.mvp.view.contract.BaseView
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.startActivity
import com.tbruyelle.rxpermissions2.RxPermissions

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), BaseView {

    private val progressDialog by lazy {
        ProgressDialog(this)
                .apply {
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    setMessage(getString(R.string.progress_dialog_waiting))
                }
    }

    override fun moveToLogin() {
        startActivity(LoginActivity::class.java)
        finish()
    }

    override fun onError(message: String?, throwable: Throwable?) {
        AlertDialog.Builder(this)
                .setMessage(message ?: getString(R.string.error_request_failure))
                .setPositiveButton(R.string.ok, null)
                .show()
    }

    fun checkLocationPermission(onGranted: (() -> Unit)? = null) {
        val rxPermissions = RxPermissions(this)
        val locationAllow = rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        if (!locationAllow) {
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_location_permission_request))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                                .subscribe { checkLocationPermission(onGranted) }
                    }
                    .setCancelable(false)
                    .show()
        } else {
            onGranted?.invoke()
        }
    }

    override fun showProgressDialog() = progressDialog.show()

    override fun dismissProgressDialog() = progressDialog.dismiss()
}