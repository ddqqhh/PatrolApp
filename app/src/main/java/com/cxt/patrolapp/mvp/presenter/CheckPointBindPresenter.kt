package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.contract.CheckPointBindView
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import io.reactivex.rxkotlin.subscribeBy

class CheckPointBindPresenter(view: CheckPointBindView) : BasePresenter<CheckPointBindView>(view) {

    fun bindCheckPoint(deviceAddress: String, checkPointName: String) {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.bindCheckPoint(uid, deviceAddress, checkPointName)
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            when (it.statusCode == CommonConst.RESPONSE_CODE_NORMAL) {
                                true -> view.onBindSuccess()
                                false -> view.onError(it.message)
                            }
                            view.dismissProgressDialog()
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        }
                )
    }

    fun replaceCheckPoint(deviceAddress: String, checkPoint: CheckPoint) {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.replaceCheckPoint(uid, deviceAddress, checkPoint.id)
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            when (it.statusCode == CommonConst.RESPONSE_CODE_NORMAL) {
                                true -> view.onBindSuccess()
                                false -> view.onError(it.message)
                            }
                            view.dismissProgressDialog()
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        }
                )
    }
}