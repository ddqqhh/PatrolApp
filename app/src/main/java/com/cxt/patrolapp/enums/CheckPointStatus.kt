package com.cxt.patrolapp.enums

enum class CheckPointStatus {
    NORMAL,
    REPAIR_REQUESTING,
    UNKNOWN;

    companion object {
        fun getStatus(index: Int) = when (index) {
            1, 3 -> NORMAL
            2 -> REPAIR_REQUESTING
            else -> UNKNOWN
        }
    }
}