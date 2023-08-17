package com.devjeong.myapplication.audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.devjeong.myapplication.R

class AudioActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.audioContainer) as NavHostFragment
        navController = navHostFragment.navController
    }
}