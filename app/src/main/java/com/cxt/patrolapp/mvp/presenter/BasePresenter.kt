package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.mvp.view.PatrolApplication
import com.cxt.patrolapp.mvp.view.contract.BaseView
import com.cxt.patrolapp.mvp.model.UserRepository

open class BasePresenter<T : BaseView>(val view: T) {

    val context = PatrolApplication.context

    fun logout() {
        UserRepository.clear()
        view.moveToLogin()
    }
}