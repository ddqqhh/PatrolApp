package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.contract.DeviceManageView
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import io.reactivex.rxkotlin.subscribeBy

class DeviceManagePresenter(view: DeviceManageView) : BasePresenter<DeviceManageView>(view) {

    fun repairRequest(checkPoint: CheckPoint) {
        view.showProgressDialog()
        val uid = UserRepository.uid
        CheckPointRepository.repairRequest(uid, checkPoint.id)
                .assignSchedulers()
                .subscribeBy(
                        onNext = {
                            when (it.statusCode == CommonConst.RESPONSE_CODE_NORMAL) {
                                true -> view.repairRequestSuccess()
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