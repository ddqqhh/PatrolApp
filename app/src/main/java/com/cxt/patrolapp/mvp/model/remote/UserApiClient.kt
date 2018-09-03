package com.cxt.patrolapp.mvp.model.remote

import com.cxt.patrolapp.mvp.model.remote.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApiClient {

    @FormUrlEncoded
    @POST("/api/getSalt")
    fun getSalt(@Field("username") account: String): Observable<BaseResponse<String>>

    @FormUrlEncoded
    @POST("/api/login")
    fun login(@Field("username") account: String,
              @Field("encrypt") passwordMd5: String): Observable<BaseResponse<Long>>

    @FormUrlEncoded
    @POST("/api/checkUser")
    fun checkUid(@Field("uid") uid: Long): Observable<BaseResponse<Unit>>
}