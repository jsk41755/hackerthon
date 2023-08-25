package com.devjeong.myapplication.main.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.audio.model.fetchAudioUrl
import com.devjeong.myapplication.databinding.FragmentAiChatBinding
import com.devjeong.myapplication.main.model.ChatBotBuilder
import com.devjeong.myapplication.main.model.Message
import com.devjeong.myapplication.main.viewmodel.SelectCelebViewModel
import com.devjeong.myapplication.util.Constants.OPEN_GOOGLE
import com.devjeong.myapplication.util.Constants.OPEN_SEARCH
import com.devjeong.myapplication.util.Constants.RECEIVE_ID
import com.devjeong.myapplication.util.Constants.SEND_ID
import com.devjeong.myapplication.util.Time
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AiChatFragment : UtilityBase.BaseFragment<FragmentAiChatBinding>(R.layout.fragment_ai_chat) {
    private val viewModel: SelectCelebViewModel by activityViewModels()
    private lateinit var speechRecognizer: SpeechRecognizer
    private var textToSpeech: TextToSpeech? = null

    private lateinit var adapter: AiChatAdapter
    private var chatresponse=""   //ai chatbot 답변

    var messagesList = mutableListOf<Message>()

    private lateinit var player: ExoPlayer

    private lateinit var speaker : String
    private var selectNum : Int = 2

    private val messageString = arrayOf<String>("하나","둘","셋")
    private var messageCount: Int = 0

    override fun onStart() {
        super.onStart()

        //In case there are messages, scroll to bottom when re-opening app
        CoroutineScope(Dispatchers.IO).launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.aiChatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()

        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideProfileButton()
    }

    private fun requestPermission() {
        // 버전 체크, 권한 허용했는지 체크
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }

    override fun FragmentAiChatBinding.onViewCreated(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedCelebName.collect { selectedCelebName ->
                speaker = getSpeakerId(selectedCelebName)
            }
        }

        recyclerView()
        clickEvents()

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")         // 언어 설정

        val builder = ExoPlayer.Builder(requireContext(), DefaultRenderersFactory(requireContext()))
        player = builder.setTrackSelector(DefaultTrackSelector(requireContext()))
            .build()

        val coroutineScope = CoroutineScope(Dispatchers.IO)

        binding?.btnSpeech!!.setOnClickListener {
            // 새 SpeechRecognizer 를 만드는 팩토리 메서드
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
            speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정
            speechRecognizer.startListening(intent)                         // 듣기 시작
        }

        val imageResourceId = getImageResourceId(viewModel.selectedCelebNum.value)
        Glide.with(binding.celebProfile.context)
            .load(imageResourceId)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24)
            .into(binding.celebProfile)

        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showBackBtnButton()
        viewModel.fetchCelebKor(viewModel.selectedCelebNum.value)
        val celebName = viewModel.selectedCelebKor.value
        mainActivity?.titleTxt?.text = "AI ${celebName}"

        coroutineScope.launch {
            val helloBotMessage = String.format(resources.getString(R.string.helloBotMessage),
                viewModel.selectedCelebKor.value
            )
            val timeStamp = Time.timeStamp()
            customBotMessage(helloBotMessage)
            delay(1000)
        }
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

    private fun getSpeakerId(id: String): String {
        return when (id) {
            "CHAEUNWOO" -> "neunwoo"
            "JEONGGUK" -> "nraewon" //래원
            "VWE" -> "njoonyoung" //준영
            "SUGAR" -> "nseungpyo" //승표
            "YEJI" -> "nyeji"
            "YUNA" -> "vyuna"
            "IMYONGWOONG" -> "ndonghyun"//혜리
            "HANI" -> "nmeow"
            else -> "nshasha" //샤샤
        }
    }

    private fun clickEvents() {
        //Send a message
        binding.btnSend.setOnClickListener {
            sendMessage()
        }
        //Scroll back to correct position when user clicks on text view
        binding.etMessage.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    binding.aiChatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(requireContext(), "음성인식 시작", Toast.LENGTH_SHORT).show()
            //binding?.tvState!!.text = "이제 말씀하세요!"
        }
        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
            //binding?.tvState!!.text = "잘 듣고 있어요."
        }
        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}
        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}
        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
            //binding?.tvState!!.text = "끝!"
        }
        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            //binding?.tvState!!.text = "에러 발생: $message"
            Toast.makeText(requireContext(), "에러 발생: $message", Toast.LENGTH_SHORT).show()
        }
        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for (i in matches!!.indices) binding?.etMessage!!.setText(matches[i])
            sendMessage()

        }
        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}
        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }

    private fun recyclerView() {
        adapter = AiChatAdapter()
        binding.aiChatRecyclerView.adapter = adapter
        binding.aiChatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /*private fun startAudioStreaming(s: String) {
        val url = BuildConfig.TTS_API_KEY + s

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            // ExoPlayer.Builder를 사용하여 ExoPlayer 인스턴스 생성
            val builder = ExoPlayer.Builder(requireContext(), DefaultRenderersFactory(requireContext()))
            player = builder.setTrackSelector(DefaultTrackSelector(requireContext()))
                .build()

            val mediaItem = MediaItem.fromUri(Uri.parse(url))

            val mediaSource = ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(requireContext(), "exoplayer-sample")
            ).createMediaSource(mediaItem)

            withContext(Dispatchers.Main){
                // ExoPlayer에 MediaSource 설정
                player.setMediaSource(mediaSource)
                // 재생 시작
                //player.playWhenReady = true
                player.prepare()
                player.play()
            }
        }
    }*/

    private fun naverClovaVoice(message: String) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            // Fetch audio URL using fetchAudioUrl function
            val audioUrl = fetchAudioUrl(requireContext(), message, speaker)
            var filePath: String? = null
            Log.d("message", message)
            val mediaItem = MediaItem.fromUri(Uri.fromFile(File(audioUrl)))

            val mediaSource = ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(requireContext(), "exoplayer-sample")
            ).createMediaSource(mediaItem)

            withContext(Dispatchers.Main) {
                filePath = audioUrl

                player.setMediaSource(mediaSource)
                player.prepare()
                player.play()

                player.addListener(object : Player.Listener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            val outputFile = filePath?.let { File(it) }
                            val result = outputFile?.deleteRecursively()
                            if (result == true) {
                                println("Deletion succeeded.")
                            } else {
                                println("Deletion failed.")
                            }
                        }
                    }
                })
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(Message(message, SEND_ID, timeStamp, isLiked=false))
            binding.etMessage.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp, isLiked=false))
            binding.aiChatRecyclerView.scrollToPosition(adapter.itemCount - 1)


            botResponse(message)
        }
    }

    private fun botResponse(message: String) {

        val timeStamp = Time.timeStamp()

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            //Fake response delay
            //delay(1000)
            var response=""

            withContext(Dispatchers.Main) {

                //Gets the response(3 case)
                Chatbotlist(message);
                response=chatresponse;

                //Adds it to our local list

                //messagesList.add(Message(response, RECEIVE_ID, timeStamp))
                messagesList.add(Message(response, RECEIVE_ID, timeStamp, isLiked=false))

                //Inserts our message into the adapter
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp, isLiked=false))
                //adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                //Scrolls us to the position of the latest message
                binding.aiChatRecyclerView.scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                }

                if(selectNum == 6){
                    //startAudioStreaming(response)
                } else {
                    naverClovaVoice(response)
                }
            }
        }
    }

    private fun customBotMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp, isLiked=false))

                val rootView = requireView()
                rootView.post{
                    adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp, isLiked=false))
                }

                binding.aiChatRecyclerView.scrollToPosition(adapter.itemCount - 1)

                if(selectNum == 6){
                    //startAudioStreaming(message)
                } else {
                    naverClovaVoice(message)
                }
            }
        }
    }

    private suspend fun Chatbotlist(s: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val startTime = System.currentTimeMillis()

                val retrofit = ChatBotBuilder.chatBotApi.getKobertResponse(s)
                val res = retrofit.execute().body()

                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime
                Log.v("응답 속도", "${duration}")
                chatresponse = messageString[messageCount]
                messageCount++
                //chatresponse= res!!.answer 이전 챗봇 구현
            }.getOrDefault(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        player.release()
    }

}