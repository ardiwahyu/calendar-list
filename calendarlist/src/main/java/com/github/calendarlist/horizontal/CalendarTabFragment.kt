package com.github.calendarlist.horizontal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.github.calendarlist.databinding.FragmentCalendarTabBinding
import java.util.*
import kotlin.math.floor

class CalendarTabFragment(
    private val selectedDate: Calendar,
    private val minDate: Calendar,
    private val maxDate: Calendar
) : Fragment() {
    private var _binding: FragmentCalendarTabBinding? = null
    private val binding get() = _binding!!
    var onDateSelected: ((Calendar) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var widthScreen: Int
        val calendarAdapter = CalendarAdapter(
            selectedDate = selectedDate,
            startDate = minDate,
            endDate = maxDate
        )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}