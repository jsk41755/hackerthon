package com.devjeong.myapplication.audio.model

data class BookListApiResponse(
    val code: Int,
    val message: String,
    val data: List<Book>
)

data class Book(
    val id: Int,
    val views: Int,
    val title: String,
    val author: String?
)

data class BookAudioApiResponse(
    val code: Int,
    val message: String,
    val data: BookAudioScript
)

data class BookAudioScript(
    val id: Int,
    val title: String,
    val author: String?,
    val views: Int,
    val count: Int,
    val contents: List<String>
)
