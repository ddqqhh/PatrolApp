package com.cxt.patrolapp.mvp.model.remote.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(@SerializedName("code") val statusCode: Int,
                           @SerializedName("msg") val message: String,
                           @SerializedName("data") val data: T?)