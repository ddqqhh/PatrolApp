package com.cxt.patrolapp.mvp.model.remote.response

import com.google.gson.annotations.SerializedName

data class PatrolScheduleResponse(@SerializedName("id") val id: Long,
                                  @SerializedName("patrolTime") val scheduleTime: String,
                                  @SerializedName("patrolName") val scheduleName: String,
                                  @SerializedName("type") val scheduleType: Int,
                                  @SerializedName("status") val scheduleStatus: Int)