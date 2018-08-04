package com.cxt.patrolapp.mvp.model

import com.cxt.patrolapp.mvp.model.remote.ApiClientCreator
import com.cxt.patrolapp.mvp.model.remote.CheckPointApiClient

object CheckPointRepository {

    private val checkPointApiClient = ApiClientCreator.createBuilder(10).create(CheckPointApiClient::class.java)

    fun bindCheckPoint(uid: Long, deviceAddress: String, checkPointName: String) = checkPointApiClient.bindCheckPoint(uid, deviceAddress, checkPointName)

    fun replaceCheckPoint(uid: Long, deviceAddress: String, checkPointId: Long) = checkPointApiClient.replaceCheckPoint(uid, deviceAddress, checkPointId)

    fun loadCheckPoint(uid: Long) = checkPointApiClient.loadCheckPoint(uid)

    fun loadCheckPointInSchedule(uid: Long, scheduleId: Long) = checkPointApiClient.loadCheckPointInSchedule(uid, scheduleId)

    fun repairRequest(uid: Long, deviceId: Long) = checkPointApiClient.repairRequest(uid, deviceId)

    fun checkIn(uid: Long, idInSchedule: Long) = checkPointApiClient.checkIn(uid, idInSchedule)
}