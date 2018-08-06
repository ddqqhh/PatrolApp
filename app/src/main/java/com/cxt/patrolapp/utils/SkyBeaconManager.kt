package com.cxt.patrolapp.utils

import android.bluetooth.BluetoothAdapter
import com.cxt.patrolapp.mvp.view.PatrolApplication
import com.skybeacon.sdk.MonitoringBeaconsListener
import com.skybeacon.sdk.RangingBeaconsListener
import com.skybeacon.sdk.ScanServiceStateCallback
import com.skybeacon.sdk.locate.SKYBeacon
import com.skybeacon.sdk.locate.SKYBeaconManager
import com.skybeacon.sdk.locate.SKYRegion

object SkyBeaconManager {

    private val ALL_SEEKCY_BEACONS_REGION = SKYRegion("rid_all", null, null, null, null)
    private val MONITOR_REGION = SKYRegion("rid", null, null, null, null)

    private var onFailed: (() -> Unit)? = null
    private var onDisconnect: (() -> Unit)? = null
    private var onFound: ((List<SKYBeacon>) -> Unit)? = null
    private var onRange: ((List<SKYBeacon>) -> Unit)? = null

    init {
        SKYBeaconManager.getInstance().init(PatrolApplication.context)
        SKYBeaconManager.getInstance().setCacheTimeMillisecond(3000)
        SKYBeaconManager.getInstance().setScanTimerIntervalMillisecond(1500)
        SKYBeaconManager.getInstance().setRangingBeaconsListener(object : RangingBeaconsListener {
            override fun onRangedNearbyBeacons(p0: SKYRegion?, p1: MutableList<Any?>?) = Unit

            override fun onRangedBeacons(p0: SKYRegion?, p1: MutableList<Any?>?) {
                p1?.filter { it is SKYBeacon }
                        ?.map { it as SKYBeacon }
                        ?.toList()
                        ?.let { onFound?.invoke(it) }
            }

            override fun onRangedBeaconsMultiIDs(p0: SKYRegion?, p1: MutableList<Any?>?) = Unit
        })
        SKYBeaconManager.getInstance().setMonitoringBeaconsListener(object : MonitoringBeaconsListener {
            override fun onExitedRegion(p0: SKYRegion?, p1: MutableList<Any?>?) {
                val result = arrayListOf<SKYBeacon>()
                p1?.filter { it is SKYBeacon }
                        ?.map { it as SKYBeacon }
                        ?.forEach { result.add(it) }
                onRange?.invoke(result)
            }

            override fun onEnteredRegion(p0: SKYRegion?, p1: MutableList<Any?>?) {
                val result = arrayListOf<SKYBeacon>()
                p1?.filter { it is SKYBeacon }
                        ?.map { it as SKYBeacon }
                        ?.forEach { result.add(it) }
                onRange?.invoke(result)
            }
        })
    }

    fun startRange(onFailed: (() -> Unit)? = null, onDisconnect: (() -> Unit)? = null, onFound: ((List<SKYBeacon>) -> Unit)? = null) {
        stop()
        SkyBeaconManager.onFailed = onFailed
        SkyBeaconManager.onDisconnect = onDisconnect
        SkyBeaconManager.onFound = onFound
        if (BluetoothAdapter.getDefaultAdapter().enable()) {
            SKYBeaconManager.getInstance().startScanService(object : ScanServiceStateCallback {
                override fun onServiceDisconnected() {
                    stop()
                    onDisconnect?.invoke()
                }

                override fun onServiceConnected() {
                    SKYBeaconManager.getInstance().startRangingBeacons(ALL_SEEKCY_BEACONS_REGION)
                }
            })
        } else {
            onFailed?.invoke()
        }
    }

    fun startMonitor(onFailed: (() -> Unit)? = null, onDisconnect: (() -> Unit)? = null, onRange: ((List<SKYBeacon>) -> Unit)? = null) {
        stop()
        SkyBeaconManager.onFailed = onFailed
        SkyBeaconManager.onDisconnect = onDisconnect
        SkyBeaconManager.onRange = onRange
        if (BluetoothAdapter.getDefaultAdapter().enable()) {
            SKYBeaconManager.getInstance().startScanService(object : ScanServiceStateCallback {
                override fun onServiceDisconnected() {
                    stop()
                    onDisconnect?.invoke()
                }

                override fun onServiceConnected() {
                    SKYBeaconManager.getInstance().startMonitoringBeacons(MONITOR_REGION)
                }
            })
        } else {
            onFailed?.invoke()
        }
    }

    fun stop() {
        onFailed = null
        onDisconnect = null
        onFound = null
        SKYBeaconManager.getInstance().stopRangingBeasons(ALL_SEEKCY_BEACONS_REGION)
        SKYBeaconManager.getInstance().stopMonitoringBeacons(MONITOR_REGION)
        SKYBeaconManager.getInstance().stopScanService()
    }
}