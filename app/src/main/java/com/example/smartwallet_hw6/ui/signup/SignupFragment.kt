package com.example.smartwallet_hw6.ui.signup

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentSignupBinding
import com.example.smartwallet_hw6.ui.SignupFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val binding by viewBinding(FragmentSignupBinding::bind)

    private lateinit var auth: FirebaseAuth

    private lateinit var selectedImage: Uri


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        with(binding) {

            tvAlreadyHaveAnAccount.setOnClickListener {
                findNavController().navigate(SignupFragmentDirections.signupToSignin())
            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            ivUserPhoto.setOnClickListener {
                selectUserPhoto()
            }

            btnSignup.setOnClickListener {
                val name = etName.text.toString()
                val surname = etSurname.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (name.isNotEmpty() && surname.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                    addUserOnDb(name, surname, email, password)
                    uploadPhoto(email)
                    signUp(email, password)
                } else {
                    Toast.makeText(requireContext(), "Fill required fields !", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }
    }

    private fun uploadPhoto(email: String) {

        val storageReference = FirebaseStorage.getInstance().getReference("images/${email}")

        storageReference.putFile(selectedImage)
            .addOnSuccessListener {
                binding.ivUserPhoto.setImageURI(selectedImage)
                Toast.makeText(requireContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error Occured During Photo Upload",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            findNavController().navigate(SignupFragmentDirections.signupToSignin())
        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
        }
    }

    private fun addUserOnDb(
        name: String,
        surname: String,
        email: String,
        password: String
    ) {
        val db = Firebase.firestore

        val user = hashMapOf(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "password" to password
        )

        db.collection("users").document(email)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "User registered successfully.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(ContentValues.TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

    }

    private fun selectUserPhoto() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectedImage = data.data!!
                binding.ivUserPhoto.setImageURI(selectedImage)
            }
        }
    }

}