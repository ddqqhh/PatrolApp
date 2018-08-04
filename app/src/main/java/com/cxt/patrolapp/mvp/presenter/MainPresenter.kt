package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.mvp.view.contract.MainView
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.PatrolScheduleRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.mapper.PatrolScheduleMapper
import io.reactivex.rxkotlin.subscribeBy

class MainPresenter(view: MainView) : BasePresenter<MainView>(view) {

    fun loadScheduleRecently() {
        view.showProgressDialog()
        val uid = UserRepository.uid
        PatrolScheduleRepository.loadSchedule(uid)
                .map { PatrolScheduleMapper.getScheduleList(it.data) }
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            view.loadPlanSuccess(it)
                            view.dismissProgressDialog()
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        })
    }
}