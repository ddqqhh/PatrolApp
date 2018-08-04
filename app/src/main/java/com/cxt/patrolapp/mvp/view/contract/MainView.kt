package com.cxt.patrolapp.mvp.view.contract

import com.cxt.patrolapp.mvp.model.entity.PatrolSchedule

interface MainView : BaseView {

    fun loadPlanSuccess(scheduleList: List<PatrolSchedule>)
}