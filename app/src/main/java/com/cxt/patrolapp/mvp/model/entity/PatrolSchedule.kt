package com.cxt.patrolapp.mvp.model.entity

import com.cxt.patrolapp.enums.PatrolScheduleStatus
import com.cxt.patrolapp.enums.PatrolScheduleType
import org.threeten.bp.LocalDateTime
import java.io.Serializable

data class PatrolSchedule(val id: Long,
                          val name: String,
                          val dateTime: LocalDateTime,
                          val status: PatrolScheduleStatus,
                          val type: PatrolScheduleType) : Serializable