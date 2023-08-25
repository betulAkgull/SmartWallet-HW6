package com.example.smartwallet_hw6.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentIncomeBinding


class IncomeFragment : Fragment(R.layout.fragment_income) {

    private val binding by viewBinding(FragmentIncomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

        }
    }
}