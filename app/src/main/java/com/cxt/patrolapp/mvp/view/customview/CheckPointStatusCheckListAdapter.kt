package com.cxt.patrolapp.mvp.view.customview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.CheckPointRepository
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.model.entity.BeaconDevice
import com.cxt.patrolapp.mvp.model.entity.CheckPoint
import com.cxt.patrolapp.mvp.model.entity.GroupOfCheckPointAndDevice
import com.cxt.patrolapp.mvp.model.remote.response.BaseResponse
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class CheckPointStatusCheckListAdapter(checkPointList: List<CheckPoint>, private val onClick: (GroupOfCheckPointAndDevice) -> Unit, private val onAutoRequest: (GroupOfCheckPointAndDevice) -> Unit) : RecyclerView.Adapter<CheckPointStatusCheckListAdapter.ViewHolder>() {

    val groupList: List<GroupOfCheckPointAndDevice> = checkPointList.map { GroupOfCheckPointAndDevice(it) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val itemView = CheckPointStatusCheckView(parent.context)
        itemView.layoutParams = layoutParams
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getItemView().setOnClickListener { onClick.invoke(groupList[position]) }
        holder.getItemView().setCheckPoint(groupList[position])
    }

    fun inject(deviceList: List<BeaconDevice>) {
        if (deviceList.isNotEmpty()) {
            groupList.filter { it.device == null }
                    .forEach { group ->
                        val target = deviceList.firstOrNull { it.address == group.checkPoint.deviceAddress }
                        if (target != null) {
                            group.device = target
                            if (target.isError) { onAutoRequest.invoke(group) }
                            notifyItemChanged(groupList.indexOf(group))
                        }
                    }
        }
    }

    class ViewHolder(view: CheckPointStatusCheckView) : RecyclerView.ViewHolder(view) {
        fun getItemView() = itemView as CheckPointStatusCheckView
    }
}