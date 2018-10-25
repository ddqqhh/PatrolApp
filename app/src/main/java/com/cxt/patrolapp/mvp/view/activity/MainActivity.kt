package com.cxt.patrolapp.mvp.view.activity

import android.app.AlertDialog
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.extend.toPx
import com.cxt.patrolapp.mvp.model.entity.PatrolSchedule
import com.cxt.patrolapp.mvp.presenter.MainPresenter
import com.cxt.patrolapp.mvp.view.contract.MainView
import com.cxt.patrolapp.mvp.view.customview.PatrolScheduleAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {

    private val adapter = PatrolScheduleAdapter { plan ->
        if (plan.status.isDone()) {
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.patrol_schedule_is_done))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show()
        } else {
            startActivity(CheckInActivity::class.java) {
                it.putExtra(CommonConst.PATROL_PLAN, plan)
            }
        }
    }

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        plan_list_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        plan_list_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = when (parent.getChildAdapterPosition(view) == 0) {
                    true -> 45.toPx()
                    false -> 0
                }
                outRect.bottom = when (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount
                        ?: 0) - 1) {
                    true -> 120.toPx()
                    else -> 0
                }
            }
        })
        plan_list_view.adapter = adapter

        menu_button.setOnClickListener { startActivity(DeviceManageActivity::class.java) }
        refresh_button.setOnClickListener { reload() }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    override fun loadPlanSuccess(scheduleList: List<PatrolSchedule>) {
        if (scheduleList.isEmpty()) {
            no_result_icon.visibility = View.VISIBLE
            plan_list_view.visibility = View.GONE
        } else {
            no_result_icon.visibility = View.GONE
            plan_list_view.visibility = View.VISIBLE
            adapter.planList = scheduleList
            adapter.notifyDataSetChanged()
        }
    }

    private fun reload() {
        adapter.planList = listOf()
        adapter.notifyDataSetChanged()
        presenter.loadScheduleRecently()
    }
}