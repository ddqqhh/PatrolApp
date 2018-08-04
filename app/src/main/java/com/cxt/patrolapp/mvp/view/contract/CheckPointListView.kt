package com.cxt.patrolapp.mvp.view.contract

import com.cxt.patrolapp.mvp.model.entity.CheckPoint

interface CheckPointListView : BaseView {

    fun loadCheckPointSuccess(pointList: List<CheckPoint>)
}