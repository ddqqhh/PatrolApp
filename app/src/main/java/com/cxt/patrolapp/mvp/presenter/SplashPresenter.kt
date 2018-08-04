package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.mvp.view.contract.SplashView
import com.cxt.patrolapp.mvp.model.UserRepository

class SplashPresenter(view: SplashView) : BasePresenter<SplashView>(view) {

    fun checkUid() {
        when {
            UserRepository.uid < 1 -> view.moveToLogin()
            else -> view.moveToMain()
        }
    }
}