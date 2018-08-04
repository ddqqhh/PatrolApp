package com.cxt.patrolapp.mvp.view.contract

import com.cxt.patrolapp.mvp.model.entity.CheckPoint

interface CheckInView : BaseView {

    fun onLoadCheckPointSuccess(pointList: List<CheckPoint>)

    fun onCheckInSuccess()
}