package com.cxt.patrolapp.mvp.view.customview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxt.patrolapp.CommonConst
import com.cxt.patrolapp.mvp.view.PatrolApplication
import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.PatrolScheduleStatus
import com.cxt.patrolapp.extend.toString
import com.cxt.patrolapp.extend.with
import com.cxt.patrolapp.mvp.model.entity.PatrolSchedule
import kotlinx.android.synthetic.main.item_patrol_schedule.view.*

class PatrolScheduleAdapter(val onClick: (PatrolSchedule) -> Unit) : RecyclerView.Adapter<PatrolScheduleAdapter.ViewHolder>() {

    var planList = listOf<PatrolSchedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val planItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_patrol_schedule, parent, false)
        return ViewHolder(planItemView, onClick)
    }

    override fun getItemCount() = planList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(planList[position])

    class ViewHolder(view: View, private val onClick: (PatrolSchedule) -> Unit) : RecyclerView.ViewHolder(view) {

        fun setContent(schedule: PatrolSchedule) {
            val dateTimeString = schedule.dateTime.toString(CommonConst.DATE_TIME_FULL_WITHOUT_YEAR)
            itemView.schedule_text.text = PatrolApplication.context.getString(R.string.patrol_schedule_title_format)
                    .with(dateTimeString)
            when (schedule.status) {
                PatrolScheduleStatus.SCHEDULE_NOT_START -> {
                    itemView.status_view.setBackgroundResource(R.drawable.shap_patrol_schedule_item_tag_not_start)
                    itemView.status_view.setText(R.string.patrol_schedule)
                }
                PatrolScheduleStatus.SCHEDULE_FINISHED -> {
                    itemView.status_view.setBackgroundResource(R.drawable.shap_patrol_schedule_item_tag_finished)
                    itemView.status_view.setText(R.string.patrol_finished)
                }
            }
            itemView.setOnClickListener { onClick.invoke(schedule) }
        }
    }
}