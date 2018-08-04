package com.cxt.patrolapp.mvp.model

import com.cxt.patrolapp.mvp.model.remote.ApiClientCreator
import com.cxt.patrolapp.mvp.model.remote.PatrolScheduleApiClient

object PatrolScheduleRepository {

    private val patrolScheduleApiClient = ApiClientCreator.createBuilder(10).create(PatrolScheduleApiClient::class.java)

    fun loadSchedule(uid: Long) = patrolScheduleApiClient.loadSchedule(uid)
}