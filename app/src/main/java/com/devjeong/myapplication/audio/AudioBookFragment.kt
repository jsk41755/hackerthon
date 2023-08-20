package com.devjeong.myapplication.audio

import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.audio.model.fetchAudioUrl
import com.devjeong.myapplication.databinding.FragmentAudioBookBinding
import com.devjeong.myapplication.main.viewmodel.SelectCelebViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.launch

class AudioBookFragment
    : UtilityBase.BaseFragment<FragmentAudioBookBinding>(R.layout.fragment_audio_book) {

    private val viewModel: AudioBookViewModel by activityViewModels()
    private lateinit var adapter: AudioBookScriptAdapter

    private lateinit var player: ExoPlayer
    private var playbackPosition: Int = 0

    private lateinit var speaker: String
    private var selectNum : Int = 2
    private var autoPlayHandler: Handler? = null
    private var isAutoPlaying: Boolean = false


    override fun FragmentAudioBookBinding.onCreateView(){
        binding.lifecycleOwner = viewLifecycleOwner
        binding.audioBookViewModel = viewModel

        lifecycleScope.launch {
            viewModel.celebName.collect{
                speaker = when (it) {
                    "CHAEUNWOO" -> "neunwoo"
                    "JEONGGUK" -> "njonghyun"
                    "VWE" -> "njoonyoung"
                    "SUGAR" -> "nkyungtae"
                    "JANGWONYOUNG" -> "nyejin"
                    "KIMCHAEWON" -> "mijin"
                    "MINJI" -> "mijin"
                    "HANI" -> "nyejin"
                    else -> "vyuna"
                }
            }
        }

        player = ExoPlayer.Builder(requireContext()).build()

        adapter = AudioBookScriptAdapter(emptyList()) { position ->
            playbackPosition = position
            fetchAudioAndPlay(playbackPosition)
            startAutoPlay()
        }

        binding.audioBookScriptRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.audioBookScriptRecyclerView.adapter = adapter

        /*lifecycleScope.launch {
            viewModel.audioBookScript.collect { audioBookScriptList ->
                val contents = audioBookScriptList.flatMap { it.contents }
                adapter = AudioBookScriptAdapter(contents)
                binding.audioBookScriptRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }*/
        binding.exoPause.setOnClickListener {
            player.pause()
        }

        binding.exoPlay.setOnClickListener {
            player.play()
        }

        lifecycleScope.launch {
            viewModel.audioBookScript.collect { audioBookScriptList ->
                val contents = audioBookScriptList.flatMap { it.contents }
                adapter.updateContents(contents)
            }
        }

        lifecycleScope.launch{
            viewModel.selectedBookId.collect{bookId ->
                viewModel.fetchBookScript(bookId)
            }
        }
    }

    private fun fetchAudioAndPlay(position: Int) {
        val content = adapter.getItem(position)

        val backgroundThread = HandlerThread("AudioFetchThread")
        backgroundThread.start()

        val backgroundHandler = Handler(backgroundThread.looper)

        backgroundHandler.post {
            val audioUrl = fetchAudioUrl(requireContext(), content, speaker)

            Handler(Looper.getMainLooper()).post {
                if (audioUrl.isNotEmpty()) {
                    val mediaItem = MediaItem.fromUri(audioUrl)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.playWhenReady = true
                    adapter.setPlayingPosition(position)
                }
            }
        }
    }

    private fun startAutoPlay() {
        isAutoPlaying = true
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    if (isAutoPlaying && playbackPosition < adapter.itemCount - 1) {
                        playbackPosition++
                        fetchAudioAndPlay(playbackPosition)
                        scrollToPositionCentered(playbackPosition)
                    } else {
                        isAutoPlaying = false
                    }
                }
            }

        })
    }

    private fun scrollToPositionCentered(position: Int) {
        val layoutManager = binding.audioBookScriptRecyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        if (position in firstVisibleItemPosition..lastVisibleItemPosition) {
            val childView = binding.audioBookScriptRecyclerView.getChildAt(position - firstVisibleItemPosition)
            val centerY = binding.audioBookScriptRecyclerView.height / 2
            val itemTop = childView.top
            val scrollToY = itemTop - centerY + childView.height / 2
            binding.audioBookScriptRecyclerView.smoothScrollBy(0, scrollToY)
        } else {
            binding.audioBookScriptRecyclerView.scrollToPosition(position)
            binding.audioBookScriptRecyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    binding.audioBookScriptRecyclerView.removeOnLayoutChangeListener(this)
                    val childView = layoutManager.findViewByPosition(position)
                    val centerY = binding.audioBookScriptRecyclerView.height / 2
                    val itemTop = childView?.top ?: 0
                    val scrollToY = itemTop - centerY + (childView?.height ?: 0) / 2
                    binding.audioBookScriptRecyclerView.smoothScrollBy(0, scrollToY)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        autoPlayHandler?.removeCallbacksAndMessages(null)

    }
}