package com.cxt.patrolapp.mvp.model.remote

import com.cxt.patrolapp.mvp.model.entity.CheckPointInScheduleResponse
import com.cxt.patrolapp.mvp.model.remote.response.BaseResponse
import com.cxt.patrolapp.mvp.model.remote.response.CheckPointResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CheckPointApiClient {

    @FormUrlEncoded
    @POST("/api/addDevice")
    fun bindCheckPoint(@Field("uid") uid: Long,
                       @Field("code") deviceAddress: String,
                       @Field("place") checkPointName: String): Observable<BaseResponse<Unit>>

    @FormUrlEncoded
    @POST("/api/replaceDevice")
    fun replaceCheckPoint(@Field("uid") uid: Long,
                          @Field("newCode") deviceAddress: String,
                          @Field("deviceId") checkPointId: Long): Observable<BaseResponse<Unit>>

    @FormUrlEncoded
    @POST("/api/searchDevice")
    fun loadCheckPoint(@Field("uid") uid: Long): Observable<BaseResponse<List<CheckPointResponse>>>

    @FormUrlEncoded
    @POST("/api/searchPatrolDevice")
    fun loadCheckPointInSchedule(@Field("uid") uid: Long,
                                 @Field("worklistId") scheduleId: Long): Observable<BaseResponse<List<CheckPointInScheduleResponse>>>

    @FormUrlEncoded
    @POST("/api/deviceRepair")
    fun repairRequest(@Field("uid") uid: Long,
                      @Field("id") deviceId: Long): Observable<BaseResponse<Unit>>

    @FormUrlEncoded
    @POST("/api/patrolSignin")
    fun checkIn(@Field("uid") uid: Long,
                @Field("patrolDeviceId") idInSchedule: Long): Observable<BaseResponse<Unit>>
}