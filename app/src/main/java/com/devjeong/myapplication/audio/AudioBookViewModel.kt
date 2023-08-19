package com.devjeong.myapplication.audio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjeong.myapplication.audio.model.Book
import com.devjeong.myapplication.audio.model.BookAudioApiResponse
import com.devjeong.myapplication.audio.model.BookAudioScript
import com.devjeong.myapplication.audio.model.RetrofitBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AudioBookViewModel :  ViewModel() {

    private val _books = MutableSharedFlow<List<Book>>()
    val books: SharedFlow<List<Book>> = _books

    private val _audioBookScript = MutableStateFlow<List<BookAudioScript>>(emptyList())
    val audioBookScript: StateFlow<List<BookAudioScript>> = _audioBookScript

    suspend fun fetchBooks() {
        val response = RetrofitBuilder.audioBookApi.getBooks()
        if (response.isSuccessful) {
            val bookList = response.body()
            if (bookList != null) {
                _books.emit(bookList)
            }
        } else {
            // Handle error
        }
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