package com.devjeong.myapplication.community

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.databinding.FragmentCommunityHomeBinding
import kotlinx.coroutines.launch

class CommunityHomeFragment : UtilityBase.BaseFragment<FragmentCommunityHomeBinding>(R.layout.fragment_community_home) {
    private val communityAdapter = CommunityAdapter()
    private val viewModel: CommunityViewModel by activityViewModels()

    override fun FragmentCommunityHomeBinding.onViewCreated(){
        binding.communityRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = communityAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.communityData.collect { communityDataList ->
                communityAdapter.setData(communityDataList)
            }
        }

        communityAdapter.onItemClickListener = { communityId ->
            viewModel.performCountRequest(getWifiMacAddress(), communityId)
            val intent = Intent(requireContext(), CommunityChatActivity::class.java)
            intent.putExtra("communityId", communityId)
            startActivity(intent)
        }

        viewModel.fetchCommunityData()

        val text = "*인기 커뮤니티"
        val spannableString = SpannableString(text)

        val startIndex = text.indexOf("*")
        val endIndex = startIndex + "*".length

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.myCelebHotPink))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.communityTxt1.text = spannableString
    }

    private fun getWifiMacAddress(): String {
        val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        Log.d("wifiInfo", wifiInfo.macAddress)
        return wifiInfo.macAddress
    }
}