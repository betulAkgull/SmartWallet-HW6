package com.example.smartwallet_hw6.ui.addedit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.invisible
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.common.visible
import com.example.smartwallet_hw6.databinding.FragmentAddEditBinding
import com.example.smartwallet_hw6.model.data.IncomeExpense
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddEditFragment : BottomSheetDialogFragment(R.layout.fragment_add_edit) {

    private val binding by viewBinding(FragmentAddEditBinding::bind)

    private val args by navArgs<AddEditFragmentArgs>()

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        with(binding) {

            var documentId = ""
            var selectedSaveType = ""

            setupAddLayout()

            args.item?.let {
                setupEditLayout(it)
                documentId = it.documentId.toString()
                if (it.type == true) {
                    actSaveType.setText("Expense")
                } else {
                    actSaveType.setText("Income")
                }
            }

            var type = true

            val saveTypeList = listOf("Income", "Expense")

            val saveTypeAdapter = ArrayAdapter(
                requireContext(),
                androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                saveTypeList
            )

            actSaveType.setAdapter(saveTypeAdapter)

            actSaveType.setOnItemClickListener { _, _, position, _ ->
                selectedSaveType = saveTypeList[position]
                if (selectedSaveType == "Income") {
                    type = false
                    setRadioGrupIncome()
                } else {
                    setRadioGrupExpense()
                }
            }


            var category = ""
            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    rbCategory1.id -> category = rbCategory1.text.toString()
                    rbCategory2.id -> category = rbCategory2.text.toString()
                    rbCategory3.id -> category = rbCategory3.text.toString()
                }
            }


            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val date = etDate.text.toString()
                val cost = etCost.text.toString().toDouble()
                saveIncomeExpenseOnDb(title, date, cost, type, category)
            }

            btnSave.setOnClickListener {
                val title = etTitle.text.toString()
                val date = etDate.text.toString()
                val cost = etCost.text.toString().toDouble()
                updateIncomeExpense(documentId, title, date, cost, type, category)
            }
        }
    }


    fun setupEditLayout(it: IncomeExpense) {
        with(binding) {
            etTitle.setText(it.title)
            etDate.setText(it.date)
            etCost.setText(it.cost.toString())
            btnSave.visible()
            btnAdd.invisible()
            tvHeader.setText("Edit")

        }
    }

    fun setupAddLayout() {

        with(binding) {
            btnSave.invisible()
        }

    }

    fun setRadioGrupExpense() {
        with(binding) {
            rbCategory1.setText("Bills")
            rbCategory2.setText("Rent")
            rbCategory3.setText("Shopping")
        }
    }

    fun setRadioGrupIncome() {
        with(binding) {
            rbCategory1.setText("Salary")
            rbCategory2.setText("Rent")
            rbCategory3.setText("Crypto")
        }
    }


    private fun updateIncomeExpense(
        documentId: String,
        title: String,
        date: String,
        cost: Double,
        type: Boolean,
        category: String
    ) {

        val incomeExpense = mapOf(
            "title" to title,
            "date" to date,
            "cost" to cost,
            "type" to type,
            "category" to category
        )

        db = Firebase.firestore

        db.collection("users").document(auth.currentUser!!.email.toString())
            .collection("income_expense").document(documentId).update(incomeExpense)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Document Updated", Toast.LENGTH_SHORT).show()
                this.dismiss()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveIncomeExpenseOnDb(
        title: String,
        date: String,
        cost: Double,
        type: Boolean,
        category: String
    ) {

        val incomeExpense = mapOf(
            "title" to title,
            "date" to date,
            "cost" to cost,
            "type" to type,
            "category" to category
        )

        db = Firebase.firestore

        db.collection("users").document(auth.currentUser!!.email.toString())
            .collection("income_expense").add(incomeExpense)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Document Added", Toast.LENGTH_SHORT).show()
                this.dismiss()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Adding Failed", Toast.LENGTH_SHORT).show()
            }

    }
}