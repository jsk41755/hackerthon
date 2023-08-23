package com.devjeong.myapplication.main.view

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.audio.AudioActivity
import com.devjeong.myapplication.community.CommunityActivity
import com.devjeong.myapplication.databinding.FragmentHomeBinding
import com.devjeong.myapplication.main.viewmodel.SelectCelebViewModel


class HomeFragment
    : UtilityBase.BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: SelectCelebViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    // Handle the back button event
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun FragmentHomeBinding.onCreateView(){
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBackButton()
    }

    override fun FragmentHomeBinding.onViewCreated() {
        binding.lifecycleOwner = viewLifecycleOwner

        val text = "*인기 소식을 확인해 보세요"
        val spannableString = SpannableString(text)

        val startIndex = text.indexOf("*")
        val endIndex = startIndex + "*".length

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.myCelebHotPink))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tv2.text = spannableString

        val celebNum = viewModel.fetchCelebNum(viewModel.selectedCelebName.value)
        Log.d("selectedCelebNum", viewModel.selectedCelebNum.value.toString())

        binding.audioBookBtn.setOnClickListener{
            val intent = Intent(requireContext(), AudioActivity::class.java)
            intent.putExtra("celebName", viewModel.selectedCelebName.value)
            startActivity(intent)
        }

        binding.celebChatBtn.setOnClickListener{
            viewModel.performCountRequest(getWifiMacAddress(), celebNum)
            findNavController().navigate(R.id.action_homeFragment_to_aiChatFragment)
        }
        
        binding.communityBtn.setOnClickListener {
            val intent = Intent(requireContext(), CommunityActivity::class.java)
            startActivity(intent)
        }

        binding.btRank.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rankingFragment)
        }

        binding.btCelebChange.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_selectCelebFragment)
        }
    }

    private fun getWifiMacAddress(): String {
        val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        Log.d("wifiInfo", wifiInfo.macAddress)
        return wifiInfo.macAddress
    }

}