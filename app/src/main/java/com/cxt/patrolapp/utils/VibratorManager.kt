package com.cxt.keepnightwatch.utils

import android.app.Service
import android.os.Vibrator
import com.cxt.patrolapp.mvp.view.PatrolApplication

object VibratorManager {

    private val vibrator = PatrolApplication.context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

    @Suppress("DEPRECATION")
    fun vibrate(milliseconds: Long) {
        vibrator.vibrate(milliseconds)
    }
}
