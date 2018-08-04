package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.mvp.view.contract.CheckPointListView
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.mapper.CheckPointMapper
import io.reactivex.rxkotlin.subscribeBy

class CheckPointListPresenter(view: CheckPointListView) : BasePresenter<CheckPointListView>(view) {

    fun loadCheckPoint() {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.loadCheckPoint(uid)
                .map { CheckPointMapper.getCheckPointList(it.data) }
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            view.loadCheckPointSuccess(it)
                            view.dismissProgressDialog()
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        })
    }
}