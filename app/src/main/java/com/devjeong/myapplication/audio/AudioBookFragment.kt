package com.devjeong.myapplication.audio

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.databinding.FragmentAudioBookBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AudioBookFragment
    : UtilityBase.BaseFragment<FragmentAudioBookBinding>(R.layout.fragment_audio_book) {

    private val viewModel: AudioBookViewModel by activityViewModels()
    private lateinit var adapter: AudioBookScriptAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun FragmentAudioBookBinding.onCreateView(){
        binding.lifecycleOwner = viewLifecycleOwner
        binding.audioBookViewModel = viewModel

        adapter = AudioBookScriptAdapter(emptyList())
        binding.audioBookScriptRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.audioBookScriptRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.audioBookScript.collect { audioBookScriptList ->
                val contents = audioBookScriptList.flatMap { it.contents }
                adapter = AudioBookScriptAdapter(contents)
                binding.audioBookScriptRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        val bookId = 1
        viewModel.fetchBookScript(bookId)

    }

}