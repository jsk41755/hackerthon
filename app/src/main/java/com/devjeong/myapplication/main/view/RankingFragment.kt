package com.devjeong.myapplication.main.view

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
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
    private val rankPointList = arrayOf<String>("2024K","3011K","1704K","2290K","1104K", "3802K", "2404K", "4121K","2590K")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideProfileButton()
    }

    override fun FragmentRankingBinding.onCreateView(){

        // Initialize RecyclerView adapter
        rankAdapter = RankingAdapter(rankList, rankPointList)
        binding.rvCelebRank.adapter = rankAdapter
    }

    override fun FragmentRankingBinding.onViewCreated(){
        val text = "*전체 랭킹"
        val spannableString = SpannableString(text)

        val startIndex = text.indexOf("*")
        val endIndex = startIndex + "*".length

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.myCelebHotPink))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.myCelebRankTxt1.text = spannableString

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rankPlace.collect{newRankPlace ->
                binding.celebrityRank = "${newRankPlace}위"
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rankDescription.collect{newRankDescription ->
                binding.myCelebNick.text = newRankDescription
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rankVariance.collect{newRankVariance ->
                if (newRankVariance > 0){
                    binding.myCelebRankNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.popularity_up, 0, 0, 0)
                    binding.myCelebRankNum.text = newRankVariance.toString()
                    binding.myCelebRankNum.setTextColor(Color.RED)
                } else if(newRankVariance == 0){
                    binding.myCelebRankNum.text = "-"
                    binding.myCelebRankNum.setTextColor(ContextCompat.getColor(binding.root.context, R.color.imy_chart_gray))
                } else{
                    var minus = newRankVariance.toString()
                    minus = minus.replace("-","")
                    binding.myCelebRankNum.text = minus
                    binding.myCelebRankNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.popularity_down, 0, 0, 0)
                    binding.myCelebRankNum.setTextColor(Color.BLUE)
                }
            }
        }
    }

    private fun settingMyCeleb() {
        viewModel.fetchCelebKor(viewModel.selectedCelebNum.value)
        binding.celebName = viewModel.selectedCelebKor.value
        binding.myCelebPoint.text = rankPointList[viewModel.selectedCelebNum.value-1]

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
            7 -> R.drawable.imyoungwoong
            8 -> R.drawable.hani
            else -> R.drawable.daniel
        }
    }
}