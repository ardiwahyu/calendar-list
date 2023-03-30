package com.github.calendarlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.calendarlist.databinding.BottomsheetCalendarBinding
import com.github.fragment.CalendarFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class CalendarBottomSheet(
    private val selectedDate: Calendar,
    private val onDateSelectedListener: OnDateSelected
): BottomSheetDialogFragment() {
    private var _binding: BottomsheetCalendarBinding? = null
    private val binding get() = _binding!!

    var minDate: Calendar = Calendar.getInstance().apply { add(Calendar.YEAR, -1) }
    var maxDate: Calendar = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }
    var disable: CalendarFragment.Companion.DISABLE = CalendarFragment.Companion.DISABLE.BEFORE
    var autoDismiss = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivClose.setOnClickListener { dismiss() }
        val calendarFragment = CalendarFragment(selectedDate, minDate, maxDate, disable)
        calendarFragment.onDateSelected = { cal ->
            dialog?.setOnDismissListener {
                onDateSelectedListener.onDateSelected(cal)
            }
            if (autoDismiss) dismiss()
        }
        dialog?.setOnShowListener {
            childFragmentManager.beginTransaction()
                .replace(binding.fcvContainer.id, calendarFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun build(selectedDate: Calendar, onDateSelectedListener: OnDateSelected) =
            Builder(selectedDate, onDateSelectedListener).build()
    }

    private class Builder(
        private val selectedDate: Calendar,
        private val onDateSelectedListener: OnDateSelected
    ) {
        fun build(): CalendarBottomSheet {
            return CalendarBottomSheet(selectedDate, onDateSelectedListener)
        }
    }
}