package com.github.calendarlist.vertical

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.calendarlist.R
import com.github.calendarlist.databinding.LayoutCalendarDayBinding
import com.github.calendarlist.utils.Disable
import java.util.*
import java.util.Calendar.*


class DayAdapter constructor(
    private var calendarToBind: Long,
    private var selectedDate: Long,
    private val disable: Disable
) : ListAdapter<Int, DayAdapter.ViewHolder>(diffUtils){

    private lateinit var context: Context
    private lateinit var calendar: Calendar
    private lateinit var calToBind: Calendar
    private lateinit var calSelected: Calendar
    var onClick: ((Calendar, TextView) -> Unit)? = null
    var onFindDateSelected: ((TextView) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCalendarDayBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        calendar = getInstance()
        calSelected = getInstance().apply { timeInMillis = selectedDate }

        Log.d("haloo", "calendar ${calSelected.get(DAY_OF_MONTH)} ${calSelected.get(MONTH)} ${calSelected.get(
            YEAR)}")
        return ViewHolder(binding)
    }

    fun bind(): DayAdapter {
        calToBind = getInstance().apply { timeInMillis = calendarToBind }
        calToBind.set(DAY_OF_MONTH, 1)
        val maxDate = calToBind.getActualMaximum(DAY_OF_MONTH)
        val startDate = calToBind.get(DAY_OF_WEEK)
        val indexDay = arrayOf(2,3,4,5,6,7,1)
        val cells = arrayListOf<Int>()
        for (i in 0 until indexDay.indexOf(startDate)) { cells.add(0) }
        for (i in 1 .. maxDate) { cells.add(i) }
        submitList(cells)
        return this
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val item = getItem(position).toString()
            if (item == "0") {
                holder.binding.calendarDayText.setTextColor(Color.WHITE)
            } else {
                val notAvailable =
                    if (disable == Disable.AFTER) {
                        calToBind.get(YEAR) > calendar.get(YEAR) ||
                        (calToBind.get(YEAR) == calendar.get(YEAR) &&
                                calToBind.get(MONTH) > calendar.get(MONTH)) ||
                        (calToBind.get(YEAR) == calendar.get(YEAR) &&
                                calToBind.get(MONTH) == calendar.get(MONTH) &&
                                item.toInt() > calendar.get(DAY_OF_MONTH))
                    }
                    else {
                        calToBind.get(YEAR) < calendar.get(YEAR) ||
                        calToBind.get(YEAR) == calendar.get(YEAR) &&
                                calToBind.get(MONTH) < calendar.get(MONTH) ||
                        (calToBind.get(YEAR) == calendar.get(YEAR) &&
                                calToBind.get(MONTH) == calendar.get(MONTH) &&
                                item.toInt() < calendar.get(DAY_OF_MONTH))
                    }
                val now = calToBind.get(YEAR) == calendar.get(YEAR) &&
                        calToBind.get(MONTH) == calendar.get(MONTH) &&
                        calendar.get(DAY_OF_MONTH) == item.toInt()
                val dateSelected = calSelected.get(YEAR) == calToBind.get(YEAR) &&
                        calSelected.get(MONTH) == calToBind.get(MONTH) &&
                        calSelected.get(DAY_OF_MONTH) == item.toInt()
                if (now) {
                    holder.binding.calendarDayText.background = ContextCompat.getDrawable(context,
                        R.drawable.shape_circle_now
                    )
                }
                if (dateSelected) {
                    onFindDateSelected?.invoke(holder.binding.calendarDayText)
                    holder.binding.calendarDayText.background = ContextCompat.getDrawable(context,
                        R.drawable.shape_circle_selected
                    )
                }

                if (position % 7 == 6) {
                    holder.binding.calendarDayText.setTextColor(ContextCompat.getColorStateList(context,
                        R.color.md_red_100
                    ))
                    if (!notAvailable) {
                        holder.binding.calendarDayText.setTextColor(Color.RED)
                        holder.binding.calendarDayText.setOnClickListener {
                            holder.binding.calendarDayText.background = ContextCompat.getDrawable(context,
                                R.drawable.shape_circle_selected
                            )
                            onClick?.invoke(calToBind.apply { set(DAY_OF_MONTH, item.toInt()) }, holder.binding.calendarDayText)
                        }
                    }
                } else {
                    holder.binding.calendarDayText.setTextColor(ContextCompat.getColorStateList(context,
                        R.color.md_blue_grey_200
                    ))
                    if (!notAvailable) {
                        holder.binding.calendarDayText.setTextColor(Color.BLACK)
                        holder.binding.calendarDayText.setOnClickListener {
                            holder.binding.calendarDayText.background = ContextCompat.getDrawable(context,
                                R.drawable.shape_circle_selected
                            )
                            onClick?.invoke(calToBind.apply { set(DAY_OF_MONTH, item.toInt()) }, holder.binding.calendarDayText)
                        }
                    }
                }
            }
            holder.binding.calendarDayText.text = item
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class ViewHolder(itemBinding: LayoutCalendarDayBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var binding: LayoutCalendarDayBinding = itemBinding
    }

    companion object {
        private val diffUtils = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
                oldItem == newItem
        }
    }
}