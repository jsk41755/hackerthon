package com.devjeong.myapplication.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SelectCelebViewModel : ViewModel() {

    private val _selectedCelebName = MutableStateFlow("")
    val selectedCelebName: StateFlow<String> = _selectedCelebName

    fun updateSelectedCelebName(celebName: String) {
        _selectedCelebName.value = celebName
        Log.d("SelectCelebViewModel", selectedCelebName.value)
    }
}
