package com.devjeong.myapplication.main.view

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.community.model.Rank
import com.devjeong.myapplication.databinding.FragmentRankingBinding
import com.devjeong.myapplication.main.viewmodel.SelectCelebViewModel
import kotlinx.coroutines.launch

class RankingFragment : UtilityBase.BaseFragment<FragmentRankingBinding>(R.layout.fragment_ranking) {
    private val viewModel: SelectCelebViewModel by activityViewModels()
    private lateinit var rankAdapter: RankingAdapter
    private val rankList: MutableList<Rank> = mutableListOf()

    override fun FragmentRankingBinding.onCreateView(){

        // Initialize RecyclerView adapter
        rankAdapter = RankingAdapter(rankList)
        binding.rvCelebRank.adapter = rankAdapter
    }

    override fun FragmentRankingBinding.onViewCreated(){
        binding.rvCelebRank.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rankAdapter
        }

        viewModel.fetchRankData()
        settingMyCeleb()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rankList.collect { newRankList ->
                rankList.clear()
                rankList.addAll(newRankList)
                rankAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun settingMyCeleb() {
        viewModel.fetchCelebKor(viewModel.selectedCelebNum.value)
        binding.celebName = viewModel.selectedCelebKor.value

        val imageResourceId = getImageResourceId(viewModel.selectedCelebNum.value)
        Glide.with(binding.celebProfile.context)
            .load(imageResourceId)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24)
            .into(binding.celebProfile)
    }

    private fun getImageResourceId(id: Int): Int {
        return when (id) {
            1 -> R.drawable.jeongguk
            2 -> R.drawable.chaeunwoo
            3 -> R.drawable.vwe
            4 -> R.drawable.sugar
            5 -> R.drawable.yeji
            6 -> R.drawable.yuna
            7 -> R.drawable.kyunglee
            8 -> R.drawable.hani
            else -> R.drawable.daniel
        }
    }
}