package com.cxt.patrolapp.extend

import android.view.View
import android.view.ViewGroup

val ViewGroup.childes: List<View>
    get() = (0 until childCount).map { getChildAt(it) }

val List<Boolean>.isAllTrue
    get() = !this.contains(false)