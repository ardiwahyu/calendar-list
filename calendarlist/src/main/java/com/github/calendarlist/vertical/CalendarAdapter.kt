package com.github.calendarlist.vertical

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.calendarlist.R
import com.github.calendarlist.databinding.LayoutCalendarBinding
import com.github.calendarlist.utils.DateUtil.Companion.parse
import com.github.calendarlist.utils.Disable
import java.util.*


class CalendarAdapter constructor(
    private val startDate: Calendar,
    private val endDate: Calendar,
    private val selectedDate: Calendar,
    private val disable: Disable
) : ListAdapter<Long, CalendarAdapter.ViewHolder>(
    diffUtils
){
    private lateinit var context: Context
    var onClick: ((Calendar) -> Unit)? = null
    private var currentDateSelected: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCalendarBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        return ViewHolder(binding)
    }

    fun bind(): CalendarAdapter {
        val listCalendar = arrayListOf<Long>()
        val cal = startDate
        var month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        while (cal.before(endDate.apply { set(Calendar.DAY_OF_MONTH, 0) })) {
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)
            listCalendar.add(cal.timeInMillis)
            month += 1
        }
        submitList(listCalendar)
        return this
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val item = getItem(position)
            holder.binding.tvMonthYear.text = item.parse("MMM yyyy")
            val dayAdapter = DayAdapter(
                calendarToBind = getItem(position),
                selectedDate = selectedDate.timeInMillis,
                disable = disable
            )
            dayAdapter.onFindDateSelected = { currentDateSelected = it }
            dayAdapter.onClick = { calendar: Calendar, textView: TextView ->
                currentDateSelected?.background = null
                currentDateSelected = textView
                textView.background = ContextCompat.getDrawable(context,
                    R.drawable.shape_circle_selected
                )
                onClick?.invoke(calendar)
            }
            holder.binding.rvCalendar.adapter = dayAdapter.bind()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class ViewHolder(itemBinding: LayoutCalendarBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var binding: LayoutCalendarBinding = itemBinding
    }

    companion object {
        private val diffUtils = object : DiffUtil.ItemCallback<Long>() {
            override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean =
                oldItem == newItem
        }
    }
}