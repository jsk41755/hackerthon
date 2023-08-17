package com.devjeong.myapplication.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devjeong.myapplication.BaseFragment
import com.devjeong.myapplication.R
import com.devjeong.myapplication.databinding.FragmentSelectCelebBinding

class SelectCelebFragment : BaseFragment<FragmentSelectCelebBinding>(R.layout.fragment_select_celeb){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_celeb, container, false)
    }
}