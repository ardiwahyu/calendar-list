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
import java.util.*


class CalendarAdapter constructor(
    private val startDate: Calendar,
    private val endDate: Calendar,
    private val selectedDate: Calendar
) : ListAdapter<Long, CalendarAdapter.ViewHolder>(
    diffUtils
){
    private lateinit var context: Context
    var onClick: ((Calendar) -> Unit)? = null
    var onFindDateSelected: ((Int) -> Unit)? = null
    private var currentDateSelected: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCalendarBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        return ViewHolder(binding)
    }

    fun bind(): CalendarAdapter {
        val listCalendar = arrayListOf<Long>()
        val calStart = Calendar.getInstance().apply {
            timeInMillis = startDate.timeInMillis
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val calEnd = Calendar.getInstance().apply {
            timeInMillis = endDate.timeInMillis
            set(Calendar.DAY_OF_MONTH, 1)
        }
        var month = calStart.get(Calendar.MONTH)
        val year = calStart.get(Calendar.YEAR)
        listCalendar.add(calStart.timeInMillis)
        while (calStart.before(calEnd)) {
            month += 1
            calStart.set(Calendar.DAY_OF_MONTH, 1)
            calStart.set(Calendar.MONTH, month)
            calStart.set(Calendar.YEAR, year)
            listCalendar.add(calStart.timeInMillis)
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
                startDate = startDate,
                endDate = endDate
            )
            dayAdapter.onFindDateSelected = {
                currentDateSelected = it
                onFindDateSelected?.invoke(position)
            }
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