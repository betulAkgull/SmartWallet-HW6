package com.example.smartwallet_hw6.ui.summary

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentSummaryBinding
import com.example.smartwallet_hw6.databinding.LogoutAlertDialogBinding
import com.example.smartwallet_hw6.model.data.IncomeExpense
import com.example.smartwallet_hw6.ui.adapter.IncomeExpenseAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SummaryFragment : Fragment(R.layout.fragment_summary) {

    private val binding by viewBinding(FragmentSummaryBinding::bind)

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

        binding.rvSummary.adapter = incomeExpenseAdapter

        getSummary()

        with(binding) {
            getUserInfo()

            ivLogout.setOnClickListener {
                showDialog()
            }

            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.addEditFragment)
            }
        }
    }

    private fun getSummary() {
        val docRef = db.collection("users").document(auth.currentUser!!.email.toString())
            .collection("income_expense")

        docRef.addSnapshotListener { snapshot, error ->
            val summaryList = arrayListOf<IncomeExpense>()

            var totalIncome = 0.0
            var totalExpense = 0.0

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
                if (document.get("type") == true) {
                    totalExpense += (document.get("cost") as Number).toDouble()
                } else {
                    totalIncome += (document.get("cost") as Number).toDouble()
                }
            }

            if (totalExpense > totalIncome) {
                binding.tvTotal.setText("Total: -${totalExpense - totalIncome}")
            } else {
                binding.tvTotal.setText("Total: +${totalIncome - totalExpense}")
            }

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
        val action = SummaryFragmentDirections.summaryToAddEdit().setItem(item)
        findNavController().navigate(action)
    }

    private fun getUserInfo() {


        val imageRef =
            FirebaseStorage.getInstance().getReference("images/${auth.currentUser!!.email}")

        var file = File.createTempFile("template", "jpeg")

        imageRef.getFile(file).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            binding.ivProfileImage.setImageBitmap(bitmap)
        }


        val docRef = db.collection("users").document(auth.currentUser!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.getString("name").toString().capitalize()
                    binding.tvUsername.text = username
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val binding = LogoutAlertDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()

        with(binding) {
            btnYes.setOnClickListener {
                auth.signOut()
                Toast.makeText(requireContext(), "Logged out.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                findNavController().navigate(R.id.splashFragment)
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}