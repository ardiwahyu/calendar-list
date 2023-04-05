package com.github.calendarlist.horizontal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.calendarlist.R
import com.github.calendarlist.databinding.LayoutCalendarTabBinding
import com.github.calendarlist.utils.DateUtil.Companion.parse
import java.util.*


class CalendarAdapter constructor(
    private var startDate: Calendar,
    private val endDate: Calendar,
    private val selectedDate: Calendar
) : ListAdapter<Long, CalendarAdapter.ViewHolder>(
    diffUtils
){
    private lateinit var context: Context
    var onClick: ((Calendar) -> Unit)? = null
    var onFindDateSelected: ((Int) -> Unit)? = null
    var width: Int = 0
    private var currentDateSelected: LayoutCalendarTabBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCalendarTabBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        return ViewHolder(binding)
    }

    fun bind(): CalendarAdapter {
        val listCalendar = arrayListOf<Long>()
        val cal = Calendar.getInstance()
        if (startDate.before(cal)) startDate = cal
        var day = startDate.get(Calendar.DAY_OF_MONTH)
        var month = startDate.get(Calendar.MONTH)
        while (startDate.before(endDate)) {
            startDate.set(Calendar.DAY_OF_MONTH, day)
            listCalendar.add(startDate.timeInMillis)
            if (month != startDate.get(Calendar.MONTH)) {
                day = 1
                month = startDate.get(Calendar.MONTH)
            }
            day += 1
        }
        submitList(listCalendar)
        return this
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val item = getItem(position)
            holder.binding.root.minimumWidth = width
            holder.binding.tvDay.text = item.parse("EEEE")
            holder.binding.tvDate.text = item.parse("dd")

            val cal = Calendar.getInstance().apply { timeInMillis = item }
            val selected = cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH) &&
                    cal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                    cal.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
            if (selected) {
                currentDateSelected = holder.binding
                currentDateSelected?.enable()
                onFindDateSelected?.invoke(position)
            } else {
                holder.binding.disable()
            }

            holder.binding.root.setOnClickListener {
                currentDateSelected?.disable()
                currentDateSelected = holder.binding
                currentDateSelected?.enable()
                onClick?.invoke(Calendar.getInstance().apply { timeInMillis = item })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun LayoutCalendarTabBinding.disable() {
        this.tvDay.setTextColor(Color.parseColor("#989898"))
        this.tvDate.setTextColor(Color.parseColor("#989898"))
        this.viewActive.visibility = View.INVISIBLE
    }

    private fun LayoutCalendarTabBinding.enable() {
        this.tvDay.setTextColor(ContextCompat.getColor(context, R.color.black))
        this.tvDate.setTextColor(ContextCompat.getColor(context, R.color.black))
        this.viewActive.visibility = View.VISIBLE
    }

    class ViewHolder(itemBinding: LayoutCalendarTabBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var binding: LayoutCalendarTabBinding = itemBinding
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