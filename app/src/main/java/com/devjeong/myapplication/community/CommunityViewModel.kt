package com.devjeong.myapplication.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjeong.myapplication.community.model.CommunityClient
import com.devjeong.myapplication.community.model.CommunityData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val _communityData = MutableStateFlow<List<CommunityData>>(emptyList())
    val communityData: StateFlow<List<CommunityData>> = _communityData

    fun fetchCommunityData() {
        viewModelScope.launch {
            try {
                val response = CommunityClient.communityApi.getCommunityCeleb()
                if (response.isSuccessful) {
                    val communityDataList = response.body()?.data ?: emptyList()
                    _communityData.emit(communityDataList)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

}
