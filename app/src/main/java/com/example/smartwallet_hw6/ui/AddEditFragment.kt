package com.example.smartwallet_hw6.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentAddEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddEditFragment : BottomSheetDialogFragment(R.layout.fragment_add_edit) {

    private val binding by viewBinding(FragmentAddEditBinding::bind)

    private val args by navArgs<AddEditFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            args.item?.let {
                binding.etTitle.setText(it.title)
                binding.etDate.setText(it.date)
                binding.etCost.setText(it.cost.toString())
            }

            btnAdd.setOnClickListener {

            }

            btnSave.setOnClickListener {

            }
        }
    }
}