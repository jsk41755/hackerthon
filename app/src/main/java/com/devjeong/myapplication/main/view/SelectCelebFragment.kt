package com.devjeong.myapplication.main.view

import android.graphics.Color
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun FragmentSelectCelebBinding.onCreateView(){

        val celebButtons = mapOf(
            SelectCelebModel.CHAEUNWOO to binding.selectCelebChaeunWoo,
            SelectCelebModel.JEONGGUK to binding.selectCelebJeongguk,
            SelectCelebModel.VWE to binding.selectCelebVwe,
            SelectCelebModel.SUGAR to binding.selectCelebSugar,
            SelectCelebModel.JANGWONYOUNG to binding.selectCelebJangwonyong,
            SelectCelebModel.KIMCHAEWON to binding.selectCelebKimchaewon,
            SelectCelebModel.MINJI to binding.selectCelebMinji,
            SelectCelebModel.HANI to binding.selectCelebHani,
            SelectCelebModel.RIA to binding.selectCelebRia
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
            SelectCelebModel.JANGWONYOUNG -> binding.selectCelebJangwonyong
            SelectCelebModel.KIMCHAEWON -> binding.selectCelebKimchaewon
            SelectCelebModel.MINJI -> binding.selectCelebMinji
            SelectCelebModel.HANI -> binding.selectCelebHani
            SelectCelebModel.RIA -> binding.selectCelebRia
        }

        selectedCelebButton?.borderColor = Color.BLACK
        clickedButton.borderColor = Color.YELLOW

        selectedCelebButton = clickedButton
    }



    override fun FragmentSelectCelebBinding.onViewCreated() {

    }
}