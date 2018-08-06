package com.cxt.patrolapp.mvp.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.childes

@SuppressLint("Registered")
open class BaseEditTextActivity : BaseActivity() {

    private var startX: Float = 0f
    private var startY: Float = 0f
    private var haveMove: Boolean = false
    private var editTextList: ArrayList<EditText> = arrayListOf()
    private val inputManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun setContentView(layoutResID: Int) {
        val parent = window.decorView as ViewGroup
        val rootView = LayoutInflater.from(this).inflate(layoutResID, parent, false)
        setContentView(rootView)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        (view as ViewGroup).childes.filter { it is EditText }
                .map { it as EditText }
                .forEach { editTextList.add(it) }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(startX - ev.x) > 20 && Math.abs(startY - ev.y) > 20) {
                    haveMove = true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!haveMove) {
                    haveMove = false
                    when (editTextList.inTouch(ev)) {
                        true -> openSoftInput()
                        false -> hideSoftInput()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun Collection<EditText>.inTouch(ev: MotionEvent): Boolean = this.map {
        val leftTop = intArrayOf(0, 0).apply { it.getLocationInWindow(this) }
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + it.height
        val right = left + it.width
        val editable = ev.rawX in left..right && ev.rawY in top..bottom
        it.isFocusable = editable
        it.isCursorVisible = editable
        it.isFocusableInTouchMode = editable
        if (editable) {
            it.requestFocus()
            it.requestFocusFromTouch()
        }
        editable
    }.contains(true)

    private fun openSoftInput() = inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    private fun hideSoftInput() = inputManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)

    override fun finish() {
        hideSoftInput()
        super.finish()
    }
}