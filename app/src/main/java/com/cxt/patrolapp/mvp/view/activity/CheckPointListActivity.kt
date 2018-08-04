package com.cxt.patrolapp.mvp.view.activity

import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.contract.CheckPointListView
import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.CheckPointStatus
import com.cxt.patrolapp.extend.toPx
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.presenter.CheckPointListPresenter
import kotlinx.android.synthetic.main.activity_check_point_list.*

class CheckPointListActivity : BaseActivity(), CheckPointListView {

    private val presenter = CheckPointListPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_point_list)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_arrow, null)
        toolbar.setNavigationOnClickListener { finish() }

        presenter.loadCheckPoint()
    }

    override fun loadCheckPointSuccess(pointList: List<CheckPoint>) {
        if (pointList.isEmpty()) {
            scroll_view.visibility = View.GONE
            no_result_icon.visibility = View.VISIBLE
        } else {
            scroll_view.visibility = View.VISIBLE
            no_result_icon.visibility = View.GONE

            val itemHeight = 50.toPx()
            val textSize = 15.toFloat()
            val textColor = ContextCompat.getColor(this, R.color.gray_333333)
            val startPadding = 30.toPx()
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight)
            val toRepairRequest: (CheckPoint) -> Unit = {
                setResult(CommonConst.GET_CHECK_POINT_RESULT, intent.putExtra(CommonConst.CHECK_POINT, it))
                finish()
            }
            pointList.forEach { checkPoint ->
                val statusString = when (checkPoint.status) {
                    CheckPointStatus.NORMAL -> "<font color='#33cc66'>${getString(R.string.normal)}</font>"
                    else -> "<font color='#ff5555'>${getString(R.string.trouble)}</font>"
                }
                TextView(this).apply {
                    this.setTextColor(textColor)
                    this.layoutParams = layoutParams
                    this.text = Html.fromHtml("$statusString　${checkPoint.checkPointName}")
                    this.gravity = Gravity.CENTER_VERTICAL
                    this.setPadding(startPadding, 0, 0, 0)
                    this.textSize = textSize
                    this.setOnClickListener { toRepairRequest.invoke(checkPoint) }
                    check_point_list_view.addView(this)
                }
            }
        }
    }
}