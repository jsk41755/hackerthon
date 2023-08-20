package com.devjeong.myapplication.audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.devjeong.myapplication.R

class AudioActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var intentValue: String
    private val viewModel: AudioBookViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        intentValue = intent.getStringExtra("celebName").toString()
        viewModel.updateStringValue(intentValue)
        Log.d("speaker", viewModel.celebName.value)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.audioContainer) as NavHostFragment
        navController = navHostFragment.navController
    }
}