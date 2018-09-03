package com.cxt.patrolapp.mvp.model

import com.cxt.patrolapp.mvp.model.local.PreferenceManage
import com.cxt.patrolapp.mvp.model.remote.ApiClientCreator
import com.cxt.patrolapp.mvp.model.remote.UserApiClient

object UserRepository {

    private const val USER_ID = "uid"

    private val userApiClient = ApiClientCreator.createBuilder(10).create(UserApiClient::class.java)

    var uid: Long
        get() = PreferenceManage.getLong(USER_ID)
        set(value) {
            PreferenceManage.putLong(USER_ID, value)
        }

    fun clear() {
        PreferenceManage.remove(USER_ID)
    }

    fun getSalt(account: String) = userApiClient.getSalt(account)

    fun checkUid(uid: Long) = userApiClient.checkUid(uid)
    
    fun login(account: String, passwordMd5: String) = userApiClient.login(account, passwordMd5)
}