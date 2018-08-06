package com.cxt.patrolapp.enums

import android.content.Context
import android.net.ConnectivityManager
import com.cxt.patrolapp.mvp.view.PatrolApplication

enum class NetType {

    NO_NET,
    WIFI,
    NOT_WIFI;

    companion object {

        private fun getType(): NetType {
            val connectivityManager = PatrolApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val networkInfo = connectivityManager?.activeNetworkInfo
            if (networkInfo == null || !networkInfo.isConnected) {
                return NO_NET
            }
            return when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> WIFI
                else -> NOT_WIFI
            }
        }

        val isConnected get() = getType() != NO_NET
    }
}