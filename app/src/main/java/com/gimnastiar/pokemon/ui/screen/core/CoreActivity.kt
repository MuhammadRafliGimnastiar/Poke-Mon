package com.gimnastiar.pokemon.ui.screen.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.databinding.ActivityCoreBinding
import com.gimnastiar.pokemon.utils.False
import com.gimnastiar.pokemon.utils.True
import com.gimnastiar.pokemon.utils.setVisibleOrGone
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoreBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.itemIconTintList = null

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailFragment -> binding.bottomNavigation.setVisibleOrGone(Boolean.False)

                else -> binding.bottomNavigation.setVisibleOrGone(Boolean.True)
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }
}