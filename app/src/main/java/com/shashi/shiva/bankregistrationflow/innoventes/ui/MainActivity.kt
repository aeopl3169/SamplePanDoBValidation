package com.shashi.shiva.bankregistrationflow.innoventes.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shashi.shiva.bankregistrationflow.innoventes.R
import com.shashi.shiva.bankregistrationflow.innoventes.databinding.ActivityMainBinding
import com.shashi.shiva.bankregistrationflow.innoventes.utils.Constants.Companion.TIME_DELAY_TO_VALIDATE
import com.shashi.shiva.bankregistrationflow.innoventes.viewmodel.MainActivityViewModel
import com.shashi.shiva.bankregistrationflow.innoventes.viewmodel.MainActivityViewModelFactory
import com.shashi.shiva.bankregistrationflow.repository.MainRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModelFlow by lazy {
        ViewModelProvider(
            this,
            MainActivityViewModelFactory(application, MainRepository())
        )[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        initObserver()
        initClickListeners()

        var job: Job? = null
        binding.editTextPanNumber.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(TIME_DELAY_TO_VALIDATE)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
//                        Toast.makeText(this@MainActivity, "Pan valid", Toast.LENGTH_SHORT).show()
                        viewModelFlow.setPan(editable.toString())
                    }
                }
            }
        }
        val string1 = resources.getString(R.string.description)
        val spannableString = SpannableString(string1)
        val foregroundColorSpan = ForegroundColorSpan(resources.getColor(R.color.purple_500))
        spannableString.setSpan(foregroundColorSpan, 117, 127, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        binding.textViewDescription.setText(spannableString)

        viewModelFlow.checkAllFields.observe(this, Observer {
            if (it == true) {
                binding.buttonNext.setEnabled(true)
            } else {
                binding.buttonNext.setEnabled(false)
            }
        })
        binding.buttonNext.setEnabled(false)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModelFlow.dayOfMonth.collect {
                binding.editTextDate.setText(it)
            }
        }
        lifecycleScope.launch {
            viewModelFlow.month.collect {
                binding.editTextMonth.setText(it)
            }
        }
        lifecycleScope.launch {
            viewModelFlow.year.collect {
                binding.editTextYear.setText(it)
            }
        }
    }

    private fun initClickListeners() {
        binding.linearLayoutDob.setOnClickListener {
            showDatePicker()
        }
        binding.editTextDate.setOnClickListener {
            showDatePicker()
        }
        binding.editTextMonth.setOnClickListener {
            showDatePicker()
        }
        binding.editTextYear.setOnClickListener {
            showDatePicker()
        }
        binding.buttonNext.setOnClickListener {
            Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_LONG).show()
            finish()
        }
        binding.textViewDontPan.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { arg0, year, month, dayOfMonth ->
                viewModelFlow.setDate(dayOfMonth.toString(), month.toString(), year.toString())
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}