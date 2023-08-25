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
import com.example.smartwallet_hw6.ui.SummaryFragmentDirections
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        with(binding) {
            getUserInfo()

            ivLogout.setOnClickListener {
                showDialog()
            }

            btnAdd.setOnClickListener {
                findNavController().navigate(SummaryFragmentDirections.summaryToAddEdit())
            }
        }
    }

    private fun getUserInfo() {


        val imageRef = FirebaseStorage.getInstance().getReference("images/${auth.currentUser!!.email}")

        var file = File.createTempFile("template","jpeg")

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

        with(binding){
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