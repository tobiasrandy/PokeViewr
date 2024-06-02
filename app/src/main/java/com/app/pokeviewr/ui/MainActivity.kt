package com.app.pokeviewr.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.pokeviewr.R
import com.app.pokeviewr.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        actionBar.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.navHostContainer.id
        ) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_my_pokemon)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _: NavController, destination: NavDestination, _: Bundle? ->
            binding.bottomNavigation.isVisible = appBarConfiguration.topLevelDestinations.contains(destination.id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}