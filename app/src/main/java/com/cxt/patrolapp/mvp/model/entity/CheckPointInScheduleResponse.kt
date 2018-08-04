package com.cxt.patrolapp.mvp.model.entity

import com.google.gson.annotations.SerializedName

data class CheckPointInScheduleResponse(@SerializedName("id") val id: Long,
                                        @SerializedName("code") val deviceAddress: String?,
                                        @SerializedName("place") val checkPointName: String,
                                        @SerializedName("deviceStatus") val checkPointStatus: Int,
                                        @SerializedName("status") val checkedStatus: Int)