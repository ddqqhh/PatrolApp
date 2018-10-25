package com.cxt.patrolapp.mvp.model.entity

import java.io.Serializable

data class BeaconDevice(val uuid: String,
                        val name: String,
                        val address: String,
                        val distance: Double,
                        val battery: Int) : Serializable {

    val isError = battery < 15
}