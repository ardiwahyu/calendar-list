package com.github.calendarlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.fragment.CalendarFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val cbs = CalendarBottomSheet.build(Calendar.getInstance(), object : OnDateSelected {
//            override fun onDateSelected(calendar: Calendar) {
//                Toast.makeText(this@MainActivity, calendar.toString(), Toast.LENGTH_SHORT).show()
//            }
//        })
//        cbs.minDate = Calendar.getInstance()
//        cbs.maxDate = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }
//        cbs.disable = CalendarFragment.Companion.DISABLE.BEFORE
//        cbs.show(supportFragmentManager, null)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fcv_container, CalendarFragment(
                selectedDate = Calendar.getInstance(),
                    minDate = Calendar.getInstance().apply { add(Calendar.YEAR, 0) },
                    maxDate = Calendar.getInstance().apply { add(Calendar.YEAR, 1) },
                    disable = CalendarFragment.Companion.DISABLE.BEFORE
            )).commit()
    }
}