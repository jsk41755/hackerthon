package com.devjeong.myapplication.audio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjeong.myapplication.audio.model.Book
import com.devjeong.myapplication.audio.model.BookAudioApiResponse
import com.devjeong.myapplication.audio.model.BookAudioScript
import com.devjeong.myapplication.audio.model.BookListApiResponse
import com.devjeong.myapplication.audio.model.RetrofitBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State


class AudioBookViewModel :  ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>>
        get() = _books

    private val _audioBookScript = MutableStateFlow<List<BookAudioScript>>(emptyList())
    val audioBookScript: StateFlow<List<BookAudioScript>>
        get() = _audioBookScript

    private val _celebName = MutableStateFlow("")
    val celebName: StateFlow<String>
        get() = _celebName

    private val _selectedBookId = MutableStateFlow<Int>(0)
    val selectedBookId: StateFlow<Int>
        get() = _selectedBookId

    suspend fun fetchBooks() {
        val response = RetrofitBuilder.audioBookApi.getBooks()
        if (response.isSuccessful) {
            val apiResponse = response.body() as BookListApiResponse
            if (apiResponse != null) {
                val bookList = apiResponse.data // 실제 책 정보 배열
                _books.value = bookList // 값을 변경하려면 value 프로퍼티를 사용
            }
        } else {
            // Handle error
        }
    }

    fun setSelectedBookId(bookId: Int) {
        _selectedBookId.value = bookId
    }

    fun updateStringValue(celebName: String) {
        _celebName.value = celebName
    }

    fun fetchBookScript(bookId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitBuilder.audioBookApi.getAudioBookScript(bookId)
                if (response.isSuccessful) {
                    val apiResponse = response.body() as BookAudioApiResponse
                    _audioBookScript.value = listOf(apiResponse.data)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}