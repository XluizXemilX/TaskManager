package com.example.taskmanager.parentUI.taskCreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.appeaser.sublimepickerlibrary.SublimePicker
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.example.taskmanager.R

class SublimePickerDialogFragment: DialogFragment() {

    var recurrenceListener : IRecurrenceTask? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var mListener = object : SublimeListenerAdapter() {

            override fun onCancelled() {
                dismiss()
            }

            override fun onDateTimeRecurrenceSet(sublimeMaterialPicker: SublimePicker?, selectedDate: SelectedDate?, hourOfDay: Int, minute: Int, recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?, recurrenceRule: String?) {

                recurrenceListener?.onRecurrenceSet(selectedDate, hourOfDay, minute, recurrenceOption)

                recurrenceRule?.let {

                }


                recurrenceOption?.let {
                    // Do something with recurrenceOption
                    // Call to recurrenceOption.toString() to get recurrenceOption as a String
                }
                dismiss()
            }
        }
        var sublimePicker = SublimePicker(context)
        var sublimeOptions = SublimeOptions() // This is optional
        sublimeOptions.pickerToShow = SublimeOptions.Picker.REPEAT_OPTION_PICKER // I want the recurrence picker to show.
        var options:Int = SublimeOptions.ACTIVATE_RECURRENCE_PICKER or  SublimeOptions.ACTIVATE_DATE_PICKER or  SublimeOptions.ACTIVATE_TIME_PICKER
        sublimeOptions.setDisplayOptions(options) // I only want the recurrence picker, not the date/time pickers.
        sublimePicker.initializePicker(sublimeOptions,mListener)
        return sublimePicker
    }

    interface IRecurrenceTask {
        fun onRecurrenceSet(selectedDate: SelectedDate?, hourOfDay: Int, minute: Int, recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?){

        }
    }
}