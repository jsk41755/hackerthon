package com.devjeong.myapplication.community.model.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommentPagingSourceFactory(
    private val repository : CommunityPagingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommunityChatViewModel(repository) as T
    }
}