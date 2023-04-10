package com.github.calendarlist.horizontal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import com.github.calendarlist.databinding.FragmentCalendarTabBinding
import java.util.*
import kotlin.math.floor

class CalendarTabFragment(
    private var selectedDate: Calendar,
    private val minDate: Calendar,
    private val maxDate: Calendar
) : Fragment() {
    private var _binding: FragmentCalendarTabBinding? = null
    private val binding get() = _binding!!
    var onDateSelected: ((Calendar) -> Unit)? = null
    private val calendarAdapter by lazy {
        CalendarAdapter(
            selectedDate = selectedDate,
            startDate = minDate,
            endDate = maxDate
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var widthScreen: Int
        calendarAdapter.onFindDateSelected = {
            binding.rvCalendar.scrollToPosition(it + (it - floor(it/2.toDouble()).toInt()))
        }
        calendarAdapter.onClick = { onDateSelected?.invoke(it) }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                widthScreen = binding.root.width
                calendarAdapter.width = widthScreen/5
                binding.rvCalendar.adapter = calendarAdapter.bind()
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    fun changeDateSelected(timeInMillis: Long) {
        selectedDate = Calendar.getInstance().apply { setTimeInMillis(timeInMillis) }
        binding.rvCalendar.adapter = calendarAdapter.changeDateSelected(timeInMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}