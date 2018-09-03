package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.view.contract.SplashView
import com.cxt.patrolapp.mvp.model.UserRepository
import io.reactivex.rxkotlin.subscribeBy

class SplashPresenter(view: SplashView) : BasePresenter<SplashView>(view) {

    fun checkUid() {
        val uid = UserRepository.uid
        when {
            uid < 1 -> view.moveToLogin()
            else -> UserRepository.checkUid(uid)
                    .assignSchedulers()
                    .subscribeBy(onNext = {
                        when (it.statusCode) {
                            CommonConst.RESPONSE_CODE_NORMAL -> view.moveToMain()
                            else -> view.moveToLogin()
                        }
                    }, onError = { view.onError(throwable = it) })
        }
    }
}