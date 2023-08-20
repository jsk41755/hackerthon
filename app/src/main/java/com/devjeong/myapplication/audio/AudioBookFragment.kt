package com.devjeong.myapplication.audio

import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.audio.model.fetchAudioUrl
import com.devjeong.myapplication.databinding.FragmentAudioBookBinding
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

    private val speaker: String = "vyuna"
    private var selectNum : Int = 2
    private var autoPlayHandler: Handler? = null
    private val autoPlayDelay: Long = 5000 // 5 seconds
    private var isAutoPlaying: Boolean = false


    override fun FragmentAudioBookBinding.onCreateView(){
        binding.lifecycleOwner = viewLifecycleOwner
        binding.audioBookViewModel = viewModel

        player = SimpleExoPlayer.Builder(requireContext()).build()

        //adapter = AudioBookScriptAdapter(emptyList())
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

        lifecycleScope.launch {
            viewModel.audioBookScript.collect { audioBookScriptList ->
                val contents = audioBookScriptList.flatMap { it.contents }
                adapter.updateContents(contents) // Adapter의 updateContents 메서드 호출
            }
        }

        val bookId = 2
        viewModel.fetchBookScript(bookId)
    }

    private fun fetchAudioAndPlay(position: Int) {
        val content = adapter.getItem(position)

        // Create a new background thread
        val backgroundThread = HandlerThread("AudioFetchThread")
        backgroundThread.start()

        val backgroundHandler = Handler(backgroundThread.looper)

        backgroundHandler.post {
            val audioUrl = fetchAudioUrl(requireContext(), content, speaker)

            // Use the main thread to play the audio
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
                        // Move to the next item and play
                        playbackPosition++
                        fetchAudioAndPlay(playbackPosition)
                    } else {
                        // Playback has finished, reset auto play
                        isAutoPlaying = false
                    }
                }
            }

            // ... (다른 콜백 메서드들)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        autoPlayHandler?.removeCallbacksAndMessages(null)

    }
}