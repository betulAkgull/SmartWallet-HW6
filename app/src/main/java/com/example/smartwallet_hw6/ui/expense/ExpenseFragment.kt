package com.example.smartwallet_hw6.ui.expense

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentExpenseBinding
import com.example.smartwallet_hw6.model.data.IncomeExpense
import com.example.smartwallet_hw6.ui.ExpenseFragmentDirections
import com.example.smartwallet_hw6.ui.adapter.IncomeExpenseAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ExpenseFragment : Fragment(R.layout.fragment_expense) {

    private val binding by viewBinding(FragmentExpenseBinding::bind)

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val incomeExpenseAdapter by lazy {
        IncomeExpenseAdapter(
            ::onItemClick,
            ::onDeleteClick
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.rvExpense.adapter = incomeExpenseAdapter

        getExpenses()

        with(binding) {

        }
    }

    private fun getExpenses() {
        val docRef = db.collection("users").document(auth.currentUser!!.email.toString())
            .collection("income_expense").whereEqualTo("type", true)

        docRef.addSnapshotListener { snapshot, error ->
            val summaryList = arrayListOf<IncomeExpense>()

            var totalExpenses = 0.0

            snapshot?.forEach { document ->
                summaryList.add(
                    IncomeExpense(
                        document.id,
                        document.get("title") as String,
                        (document.get("cost") as Number).toDouble(),
                        document.get("date") as String,
                        document.get("type") as Boolean
                    )
                )
                totalExpenses += (document.get("cost") as Number).toDouble()
            }

            binding.tvTotal.setText("Total: -${totalExpenses}")

            incomeExpenseAdapter.submitList(summaryList)
        }
    }

    private fun deleteItem(id: String) {
        db.collection("users").document(auth.currentUser!!.email.toString())
            .collection("income_expense").document(id).delete().addOnSuccessListener {
                Toast.makeText(requireContext(), "Item Deleted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Item Deleted Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onDeleteClick(id: String) {
        deleteItem(id)
    }

    private fun onItemClick(item: IncomeExpense) {
        val action = ExpenseFragmentDirections.expenseToAddEdit().setItem(item)
        findNavController().navigate(action)
    }
}