package com.devjeong.myapplication.main.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.databinding.FragmentHomeBinding
import com.devjeong.myapplication.main.viewmodel.SelectCelebViewModel
import kotlinx.coroutines.launch


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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedCelebName.collect { celebName ->
                val celebNameRe = when (celebName) {
                    "CHAEUNWOO" -> "은우"
                    "JEONGGUK" -> "국"
                    "VWE" -> "뷔"
                    "SUGAR" -> "슈가"
                    "JANGWONYOUNG" -> "원영"
                    "KIMCHAEWON" -> "채원"
                    "MINJI" -> "민지"
                    "HANI" -> "하니"
                    else -> "리아"
                }

                if (celebName.isNotEmpty()) {
                    val message = getString(R.string.welcome_message, celebNameRe)
                    binding.selectCelebName = message
                }

            }
        }
    }

}