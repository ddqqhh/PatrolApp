package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.contract.CheckInView
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.model.entity.PatrolSchedule
import com.cxt.patrolapp.mvp.model.mapper.CheckPointMapper
import io.reactivex.rxkotlin.subscribeBy

class CheckInPresenter(view: CheckInView) : BasePresenter<CheckInView>(view) {

    fun loadCheckPoint(patrolSchedule: PatrolSchedule) {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.loadCheckPointInSchedule(uid, patrolSchedule.id)
                .map { CheckPointMapper.getCheckPointInScheduleList(it.data) }
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            view.onLoadCheckPointSuccess(it)
                            view.dismissProgressDialog()
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        })
    }

    fun checkIn(checkPoint: CheckPoint) {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.checkIn(uid, checkPoint.idInSchedule)
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            when (it.statusCode == CommonConst.RESPONSE_CODE_NORMAL) {
                                true -> view.onCheckInSuccess()
                                false -> view.onError(it.message)
                            }
                            view.dismissProgressDialog()
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        })
    }
}