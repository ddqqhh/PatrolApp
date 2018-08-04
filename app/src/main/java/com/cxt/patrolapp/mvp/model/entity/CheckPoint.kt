package com.cxt.patrolapp.mvp.model.entity

import com.cxt.patrolapp.enums.CheckPointStatus
import java.io.Serializable

data class CheckPoint(val id: Long = 0,
                      val idInSchedule: Long = 0,
                      val deviceAddress: String,
                      val checkPointName: String,
                      val status: CheckPointStatus,
                      val isChecked : Boolean = false) : Serializable