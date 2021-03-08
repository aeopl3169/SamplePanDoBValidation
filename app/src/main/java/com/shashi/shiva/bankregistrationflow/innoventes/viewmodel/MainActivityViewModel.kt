package com.shashi.shiva.bankregistrationflow.innoventes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.shashi.shiva.bankregistrationflow.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class MainActivityViewModel(
    application: Application,
    val repository: MainRepository
) : AndroidViewModel(application) {

    private val _dayOfMonth = MutableStateFlow("")
    val dayOfMonth: StateFlow<String> = _dayOfMonth

    private val _month = MutableStateFlow("")
    val month: StateFlow<String> = _month

    private val _year = MutableStateFlow("")
    val year: StateFlow<String> = _year

    private val _pan = MutableStateFlow("")

    val checkAllFields: LiveData<Boolean> = combine(_pan, _dayOfMonth, _month, _year){ pan, dayOfMonth, month, year ->
        val isPanNotEmpty = pan.isNotEmpty() and pan.trim().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}".toRegex())
        val isDayNotEmpty = dayOfMonth.isNotEmpty()
        val isMonthNotEmpty = month.isNotEmpty()
        val isYearNotEmpty = year.isNotEmpty()
        return@combine isPanNotEmpty and isDayNotEmpty and isMonthNotEmpty and isYearNotEmpty
    }.asLiveData()

    fun setPan(pan: String){
        _pan.value = pan
    }

    fun setDate(day: String, month: String, year: String){
        _dayOfMonth.value = day
        _month.value = month
        _year.value = year
    }
}
