package com.cxt.patrolapp.mvp.view

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class PatrolApplication : Application() {

    companion object {
        lateinit var context: PatrolApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        AndroidThreeTen.init(this)
    }
}