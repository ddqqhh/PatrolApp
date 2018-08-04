package com.cxt.patrolapp.mvp.model.remote

import com.cxt.patrolapp.mvp.model.remote.response.BaseResponse
import com.cxt.patrolapp.mvp.model.remote.response.PatrolScheduleResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PatrolScheduleApiClient {

    @FormUrlEncoded
    @POST("/api/searchPatrolWorklist")
    fun loadSchedule(@Field("uid") uid: Long): Observable<BaseResponse<List<PatrolScheduleResponse>>>
}