package com.cxt.patrolapp.utils

import android.annotation.SuppressLint
import com.cxt.patrolapp.mvp.model.entity.BeaconDevice
import com.cxt.patrolapp.mvp.view.PatrolApplication
import com.minew.beacon.*
import com.minew.beacon.MinewBeaconManager

object MinewBeaconManager {

    @SuppressLint("StaticFieldLeak")
    private val manager: MinewBeaconManager = MinewBeaconManager.getInstance(PatrolApplication.context)

    fun startRange(onRange: ((List<BeaconDevice>) -> Unit)? = null) {
        manager.startScan()
        manager.setDeviceManagerDelegateListener(object : MinewBeaconManagerListener {
            override fun onRangeBeacons(beaconList: MutableList<MinewBeacon>?) {
                beaconList?.asSequence()
                        ?.map {
                            val rssi = Math.abs(it.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).floatValue)
                            val uuid = it.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).stringValue
                            val name = it.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).stringValue
                            val address = it.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).stringValue
                            val distance = Math.pow(10.0, (rssi - 59) / (10 * 2.0))
                            val battery = it.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_BatteryLevel).intValue
                            BeaconDevice(uuid, name, address, distance, battery)
                        }?.let { onRange?.invoke(it.toList()) }
            }

            override fun onUpdateState(p0: BluetoothState?) {
            }

            override fun onDisappearBeacons(p0: MutableList<MinewBeacon>?) {
            }

            override fun onAppearBeacons(p0: MutableList<MinewBeacon>?) {
            }
        })
    }

    fun stopScan() {
        manager.setDeviceManagerDelegateListener(null)
        manager.stopScan()
    }
}