package com.devjeong.myapplication.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.devjeong.myapplication.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var backBtn: ImageView // 툴바에 있는 버튼
    lateinit var titleTxt: TextView // 툴바에 있는 버튼
    lateinit var profileBtn: ImageView // 툴바에 있는 버튼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        backBtn = findViewById(R.id.backBtn)
        profileBtn = findViewById(R.id.profileIv)
        titleTxt = findViewById(R.id.titleTxt)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController
    }


    fun hideBackButton() {
        backBtn.visibility = View.GONE
    }

    fun hideProfileButton() {
        backBtn.visibility = View.GONE
    }

    fun showBackBtnButton() {
        backBtn.visibility = View.VISIBLE
    }
}