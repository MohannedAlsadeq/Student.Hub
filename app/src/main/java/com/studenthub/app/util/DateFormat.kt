package com.studenthub.app.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/** e.g. "Tue, 17 Nov 2026 03:30 PM" */
fun formatDateTime(millis: Long): String =
    SimpleDateFormat("EEE, d MMM yyyy hh:mm a", Locale.ENGLISH).format(Date(millis))

/** Opens a date picker, then a 12-hour time picker, and returns the chosen moment. */
fun Context.pickDateTime(onPicked: (Long) -> Unit) {
    val cal = Calendar.getInstance()
    DatePickerDialog(this, { _, y, m, d ->
        TimePickerDialog(this, { _, h, min ->
            cal.set(y, m, d, h, min, 0)
            onPicked(cal.timeInMillis)
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
}
