package com.cxt.patrolapp.mvp.view.activity

import android.os.Bundle
import com.cxt.patrolapp.mvp.view.contract.SplashView
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.mvp.presenter.SplashPresenter

class SplashActivity : BaseActivity(), SplashView {

    private val presenter = SplashPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission { presenter.checkUid() }
    }

    override fun moveToMain() {
        startActivity(MainActivity::class.java)
        finish()
    }
}