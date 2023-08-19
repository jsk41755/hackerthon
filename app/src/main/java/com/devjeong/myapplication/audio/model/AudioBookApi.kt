package com.devjeong.myapplication.audio.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AudioBookApi {
    @GET("books")
    suspend fun getBooks(): Response<List<Book>>

    @GET("book/{id}")
    suspend fun getAudioBookScript(@Path("id") id: Int): Response<BookAudioApiResponse>
}