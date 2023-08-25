package com.example.smartwallet_hw6.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentAddEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddEditFragment : BottomSheetDialogFragment(R.layout.fragment_add_edit) {

    private val binding by viewBinding(FragmentAddEditBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAdd.setOnClickListener {

            }

            btnSave.setOnClickListener {

            }
        }
    }
}