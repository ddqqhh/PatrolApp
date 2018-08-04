package com.cxt.patrolapp.enums

enum class PatrolScheduleStatus {

    SCHEDULE_NOT_START,
    SCHEDULE_FINISHED;

    fun isDone() = this == SCHEDULE_FINISHED

    companion object {
        fun getStatus(index: Int) = when (index) {
            1 -> SCHEDULE_FINISHED
            else -> SCHEDULE_NOT_START
        }
    }
}