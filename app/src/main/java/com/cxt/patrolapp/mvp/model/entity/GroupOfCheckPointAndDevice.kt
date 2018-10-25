package com.cxt.patrolapp.mvp.model.entity

import com.cxt.patrolapp.utils.VibratorManager

data class GroupOfCheckPointAndDevice(val checkPoint: CheckPoint) {

    var isRepairRequested: Boolean = false

    var isRequestError: Boolean = false

    var device: BeaconDevice? = null
        set(value) {
            if (value != null) {
                field = value
                VibratorManager.vibrate(200)
            }
        }
}