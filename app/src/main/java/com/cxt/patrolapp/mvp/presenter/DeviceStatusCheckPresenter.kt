package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.entity.GroupOfCheckPointAndDevice
import com.cxt.patrolapp.mvp.model.mapper.CheckPointMapper
import com.cxt.patrolapp.mvp.model.remote.response.BaseResponse
import com.cxt.patrolapp.mvp.view.contract.DeviceStatusCheckView
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class DeviceStatusCheckPresenter(view: DeviceStatusCheckView) : BasePresenter<DeviceStatusCheckView>(view) {

    fun loadCheckPoint() {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.loadCheckPoint(uid)
                .map { CheckPointMapper.getCheckPointList(it.data) }
                .assignSchedulers()
                .doFinally { view.dismissProgressDialog() }
                .subscribeBy(
                        onNext = { view.loadCheckPointSuccess(it) },
                        onError = { view.onError(throwable = it) })
    }

    fun repairRequest(group: GroupOfCheckPointAndDevice, onSuccess: () -> Unit) {
        val uid = UserRepository.uid
        CheckPointRepository.repairRequest(uid, group.checkPoint.id)
                .assignSchedulers()
                .doFinally { onSuccess.invoke() }
                .subscribeBy(
                        onNext = {
                            when (it.statusCode == CommonConst.RESPONSE_CODE_NORMAL) {
                                true -> {
                                    group.isRepairRequested = true
                                    group.isRequestError = false
                                }
                                false -> {
                                    group.isRepairRequested = false
                                    group.isRequestError = true
                                }
                            }
                        },
                        onError = {
                            group.isRepairRequested = false
                            group.isRequestError = true
                        }
                )
    }
}