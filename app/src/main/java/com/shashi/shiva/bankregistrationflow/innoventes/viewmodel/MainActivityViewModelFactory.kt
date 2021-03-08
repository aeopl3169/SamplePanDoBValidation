package com.shashi.shiva.bankregistrationflow.innoventes.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shashi.shiva.bankregistrationflow.repository.MainRepository

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(
    val app: Application,
    val repository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            app,
            repository
        ) as T
    }
}