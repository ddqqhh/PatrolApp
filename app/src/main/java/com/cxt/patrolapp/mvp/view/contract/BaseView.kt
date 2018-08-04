package com.cxt.patrolapp.mvp.view.contract

interface BaseView {

    fun moveToLogin()

    fun onError(message: String? = null, throwable: Throwable? = null)

    fun showProgressDialog()

    fun dismissProgressDialog()
}