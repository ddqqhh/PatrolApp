package com.cxt.patrolapp.mvp.model.mapper

import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.model.entity.CheckPointInScheduleResponse
import com.cxt.patrolapp.mvp.model.remote.response.CheckPointResponse

object CheckPointMapper {

    fun getCheckPointList(response: List<CheckPointResponse>?): List<CheckPoint> {
        val result = arrayListOf<CheckPoint>()
        response?.forEach {
            CheckPoint(id = it.id,
                    deviceAddress = it.deviceAddress,
                    checkPointName = it.checkPointName,
                    status = CheckPointStatus.getStatus(it.checkPointStatus))
                    .let { result.add(it) }
        }
        return result
    }

    fun getCheckPointInScheduleList(response: List<CheckPointInScheduleResponse>?): List<CheckPoint> {
        val result = arrayListOf<CheckPoint>()
        response?.filter { it.deviceAddress != null }
                ?.forEach {
                    CheckPoint(idInSchedule = it.id,
                            deviceAddress = it.deviceAddress!!,
                            checkPointName = it.checkPointName,
                            status = CheckPointStatus.getStatus(it.checkPointStatus),
                            isChecked = it.checkedStatus == 1)
                            .let { result.add(it) }
                }
        return result
    }
}