package com.devjeong.myapplication.audio

import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.audio.model.Book
import com.devjeong.myapplication.databinding.FragmentAudioHomeBinding
import kotlinx.coroutines.launch
import kotlin.math.abs

class AudioHomeFragment
    : UtilityBase.BaseFragment<FragmentAudioHomeBinding>(R.layout.fragment_audio_home),
    AudioBookListAdapter.OnItemClickListener {

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var imageAdapter: ImageAdapter

    private val viewModel: AudioBookViewModel by activityViewModels()
    private lateinit var bookListAdapter: AudioBookListAdapter
    override fun FragmentAudioHomeBinding.onCreateView(){
        val text = "*인기 오디오"
        val text2 = "*추천 오디오"
        val spannableString = SpannableString(text)
        val spannableString2 = SpannableString(text2)

        val startIndex = text.indexOf("*")
        val endIndex = startIndex + "*".length

        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.myCelebHotPink))
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.audioHomeTxt3.text = spannableString

        spannableString2.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.audioHomeTxt1.text = spannableString2

        init()
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })

        setupRecyclerView(this@AudioHomeFragment)
        fetchAndObserveBooks()
    }

    private fun setupRecyclerView(itemClickListener: AudioBookListAdapter.OnItemClickListener) {
        bookListAdapter = AudioBookListAdapter(emptyList(), itemClickListener) // Initialize with empty list
        binding.audioBookRecyclerView.adapter = bookListAdapter
    }

    private fun fetchAndObserveBooks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchBooks() // Fetch books data from API
            viewModel.books.collect { bookList ->
                Log.d("Collect", bookList.toString()) // 데이터 확인하기
                bookListAdapter.updateData(bookList)
            }
        }
    }

    override fun onItemClick(book: Book) {
        viewModel.setSelectedBookId(book.id)
        findNavController().navigate(R.id.action_audioHomeFragment_to_audioBookFragment)
    }

    override fun FragmentAudioHomeBinding.onViewCreated(){
        handler.postDelayed(runnable, 2000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(150))
        transformer.addTransformer{ page, position ->
            val r = 1- abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init() {
        viewPager2 = binding.audioBookViewPager
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.camellia_flower)
        imageList.add(R.drawable.wild_man)
        imageList.add(R.drawable.star_book)
        imageList.add(R.drawable.little_prince)
        imageList.add(R.drawable.buckwheat_flowers)
        imageList.add(R.drawable.last_leaf)
        imageList.add(R.drawable.rich_and_the_donkey)
        imageList.add(R.drawable.shower)

        imageAdapter = ImageAdapter(imageList, viewPager2)

        viewPager2.adapter = imageAdapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }
}