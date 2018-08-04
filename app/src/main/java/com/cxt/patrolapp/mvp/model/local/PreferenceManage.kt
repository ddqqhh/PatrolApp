package com.cxt.patrolapp.mvp.model.local

import android.preference.PreferenceManager
import com.cxt.patrolapp.mvp.view.PatrolApplication

object PreferenceManage {

    private val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(PatrolApplication.context)

    private val editor = sharedPreferences.edit()

    fun getString(key: String): String? = sharedPreferences.getString(key, null)

    fun getLong(key: String) = sharedPreferences.getLong(key, 0)

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        editor.putString(key, value).commit()
    }

    fun putLong(key: String, value: Long) {
        editor.putLong(key, value).commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).commit()
    }

    fun remove(key: String) {
        editor.remove(key).commit()
    }
}