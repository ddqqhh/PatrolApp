package com.cxt.patrolapp.enums

enum class PatrolScheduleType {

    BY_GOV,
    BY_SELF,
    UNKNOWN;

    companion object {
        fun getType(index: Int) = when (index) {
            1 -> BY_GOV
            2 -> BY_SELF
            else -> UNKNOWN
        }
    }
}