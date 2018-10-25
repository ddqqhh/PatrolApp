package com.cxt.patrolapp.mvp.view.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.toPx
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.model.entity.GroupOfCheckPointAndDevice
import com.cxt.patrolapp.mvp.presenter.DeviceStatusCheckPresenter
import com.cxt.patrolapp.mvp.view.contract.DeviceStatusCheckView
import com.cxt.patrolapp.mvp.view.customview.CheckPointStatusCheckDialog
import com.cxt.patrolapp.mvp.view.customview.CheckPointStatusCheckListAdapter
import com.cxt.patrolapp.utils.MinewBeaconManager
import kotlinx.android.synthetic.main.activity_device_status_check.*

class DeviceStatusCheckActivity : BaseActivity(), DeviceStatusCheckView {

    private val dialog = CheckPointStatusCheckDialog().apply {
        onRequestButtonClick = {
            it.isRepairRequested = false
            it.isRequestError = false
            this.refresh()
            presenter.repairRequest(it) {
                this.refresh()
            }
        }
    }

    private val presenter = DeviceStatusCheckPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_status_check)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_arrow, null)
        toolbar.setNavigationOnClickListener { finish() }

        check_point_list_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        check_point_list_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 15.toPx()
                outRect.bottom = when (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount
                        ?: 0) - 1) {
                    true -> 50.toPx()
                    else -> 0
                }
            }
        })

        presenter.loadCheckPoint()
    }

    override fun loadCheckPointSuccess(checkPointList: List<CheckPoint>) {
        if (checkPointList.isNotEmpty()) {
            val onItemClick: (GroupOfCheckPointAndDevice) -> Unit = { dialog.show(it, supportFragmentManager) }
            val onAutoRequest: (GroupOfCheckPointAndDevice) -> Unit = { repairRequest(it) }

            val adapter = CheckPointStatusCheckListAdapter(checkPointList, onItemClick, onAutoRequest)
            check_point_list_view.adapter = adapter
            adapter.notifyDataSetChanged()
            MinewBeaconManager.startRange(adapter::inject)
        }
    }

    override fun onDestroy() {
        MinewBeaconManager.stopScan()
        super.onDestroy()
    }

    private fun repairRequest(group: GroupOfCheckPointAndDevice) {
        group.device?.let { _ ->
            presenter.repairRequest(group) {
                val adapter = check_point_list_view.adapter as? CheckPointStatusCheckListAdapter
                adapter?.let {
                    it.notifyItemChanged(it.groupList.indexOf(group))
                }
            }
        }
    }
}