package com.cxt.patrolapp.mvp.model.mapper

import com.cxt.patrolapp.mvp.model.remote.response.PatrolScheduleResponse
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.enums.PatrolScheduleStatus
import com.cxt.patrolapp.enums.PatrolScheduleType
import com.cxt.patrolapp.extend.toDateTime
import com.cxt.patrolapp.mvp.model.entity.PatrolSchedule

object PatrolScheduleMapper {

    fun getScheduleList(response: List<PatrolScheduleResponse>?): List<PatrolSchedule> {
        val result = arrayListOf<PatrolSchedule>()
        response?.forEach {
            val dataTime = it.scheduleTime.toDateTime(CommonConst.DATE_TIME_FULL)
            val type = PatrolScheduleType.getType(it.scheduleType)
            val status = PatrolScheduleStatus.getStatus(it.scheduleStatus)
            PatrolSchedule(it.id, it.scheduleName, dataTime, status, type).let { result.add(it) }
        }
        return result
    }
}