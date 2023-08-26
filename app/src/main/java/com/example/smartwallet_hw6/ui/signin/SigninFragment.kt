package com.example.smartwallet_hw6.ui.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.databinding.FragmentSigninBinding
import com.example.smartwallet_hw6.ui.SigninFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SigninFragment : Fragment(R.layout.fragment_signin) {

    private val binding by viewBinding(FragmentSigninBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        with(binding) {

            btnSignIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email, password)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Email/Password do not match! Please Try Again",
                        1000
                    ).show()
                    etEmail.requestFocus()
                }
            }

            tvRegister.setOnClickListener {
                findNavController().navigate(SigninFragmentDirections.signinToSignup())
            }

            ivBackSignin.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(requireContext(), "Successfully signed in.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(SigninFragmentDirections.signinToSummary())

        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
        }
    }
}