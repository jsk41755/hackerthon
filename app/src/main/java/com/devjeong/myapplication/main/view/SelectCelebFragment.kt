package com.devjeong.myapplication.main.view

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.R
import com.devjeong.myapplication.databinding.FragmentSelectCelebBinding
import com.devjeong.myapplication.main.model.SelectCelebModel
import com.devjeong.myapplication.main.viewmodel.SelectCelebViewModel
import de.hdodenhof.circleimageview.CircleImageView

class SelectCelebFragment
    : UtilityBase.BaseFragment<FragmentSelectCelebBinding>(R.layout.fragment_select_celeb){
    private val viewModel: SelectCelebViewModel by activityViewModels()
    private var selectedCelebButton: CircleImageView? = null

    override fun FragmentSelectCelebBinding.onCreateView(){
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideBackButton()
        mainActivity?.titleTxt?.text = "셀럽 선택"

        val text = "*어떤 셀럽을 가장 좋아 하나요?"
        val spannableString = SpannableString(text)

        val startIndex = text.indexOf("*")
        val endIndex = startIndex + "*".length

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.myCelebHotPink))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tv1.text = spannableString

        val celebButtons = mapOf(
            SelectCelebModel.CHAEUNWOO to binding.selectCelebChaeunWoo,
            SelectCelebModel.JEONGGUK to binding.selectCelebJeongguk,
            SelectCelebModel.VWE to binding.selectCelebVwe,
            SelectCelebModel.SUGAR to binding.selectCelebSugar,
            SelectCelebModel.YEJI to binding.selectCelebYeji,
            SelectCelebModel.YUNA to binding.selectCelebYuna,
            SelectCelebModel.KYUNGLEE to binding.selectCelebKyunglee,
            SelectCelebModel.HANI to binding.selectCelebHani,
            SelectCelebModel.DANIEL to binding.selectCelebDaniel
        )

        for ((celeb, button) in celebButtons) {
            button.setOnClickListener {
                handleCelebButtonClick(celeb)
                viewModel.updateSelectedCelebName(celeb.name)
            }
        }

        val selectCelebBtn = binding.selectCelebBtn
        selectCelebBtn.setOnClickListener {
            findNavController().navigate(R.id.action_selectCelebFragment_to_homeFragment)
        }
    }
    private fun handleCelebButtonClick(selectedCeleb: SelectCelebModel) {

        val clickedButton: CircleImageView = when (selectedCeleb) {
            SelectCelebModel.CHAEUNWOO -> binding.selectCelebChaeunWoo
            SelectCelebModel.JEONGGUK -> binding.selectCelebJeongguk
            SelectCelebModel.VWE -> binding.selectCelebVwe
            SelectCelebModel.SUGAR -> binding.selectCelebSugar
            SelectCelebModel.YEJI -> binding.selectCelebYeji
            SelectCelebModel.YUNA -> binding.selectCelebYuna
            SelectCelebModel.KYUNGLEE -> binding.selectCelebKyunglee
            SelectCelebModel.HANI -> binding.selectCelebHani
            SelectCelebModel.DANIEL -> binding.selectCelebDaniel
        }

        selectedCelebButton?.borderColor = Color.TRANSPARENT
        selectedCelebButton?.borderWidth = 0
        clickedButton.borderWidth = 12
        clickedButton.borderColor = ContextCompat.getColor(requireContext(), R.color.myCelebThinPink)

        selectedCelebButton = clickedButton
    }



    override fun FragmentSelectCelebBinding.onViewCreated() {

    }
}