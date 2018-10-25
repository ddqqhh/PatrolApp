package com.cxt.patrolapp.mvp.view.contract

import com.cxt.patrolapp.mvp.model.entity.CheckPoint

interface DeviceStatusCheckView : BaseView {

    fun loadCheckPointSuccess(checkPointList: List<CheckPoint>)
}