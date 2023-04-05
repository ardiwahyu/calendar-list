package com.github.calendarlist.vertical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.calendarlist.databinding.FragmentCalendarBinding
import java.util.*

class CalendarFragment(
    private val selectedDate: Calendar,
    private val minDate: Calendar,
    private val maxDate: Calendar,
    private val disable: DISABLE
) : Fragment() {
    companion object {
        enum class DISABLE { AFTER, BEFORE }
    }
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    var onDateSelected: ((Calendar) -> Unit)? = null
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendarAdapter = CalendarAdapter(
            startDate = minDate,
            endDate = maxDate,
            selectedDate = selectedDate,
            disable = disable
        )
        binding.rvListCalendar.adapter = calendarAdapter.bind()
        calendarAdapter.onClick = {
            onDateSelected?.invoke(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}