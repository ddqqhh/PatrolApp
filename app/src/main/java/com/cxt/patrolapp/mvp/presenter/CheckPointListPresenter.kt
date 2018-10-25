package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.mvp.view.contract.CheckPointListView
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.mapper.CheckPointMapper
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class CheckPointListPresenter(view: CheckPointListView) : BasePresenter<CheckPointListView>(view) {

    fun loadCheckPoint() {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.loadCheckPoint(uid)
                .map { CheckPointMapper.getCheckPointList(it.data) }
                .flatMap { Observable.fromIterable(it) }
                .sorted { point1, point2 ->
                    when {
                        point1.status == CheckPointStatus.REPAIR_REQUESTING -> -1
                        point2.status == CheckPointStatus.REPAIR_REQUESTING -> 1
                        else -> 0
                    }
                }
                .toList()
                .toObservable()
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