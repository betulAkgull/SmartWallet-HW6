package com.example.smartwallet_hw6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.smartwallet_hw6.common.invisible
import com.example.smartwallet_hw6.common.viewBinding
import com.example.smartwallet_hw6.common.visible
import com.example.smartwallet_hw6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            NavigationUI.setupWithNavController(bnvMain, navHostFragment.navController)

            navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.signinFragment || destination.id == R.id.splashFragment || destination.id == R.id.signupFragment) {
                    bnvMain.invisible()
                } else {
                    bnvMain.visible()
                }
            }
        }
    }
}