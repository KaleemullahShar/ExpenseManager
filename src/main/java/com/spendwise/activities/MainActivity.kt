package com.spendwise.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.spendwise.R
import com.spendwise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
        binding.bottomNav.setupWithNavController(navController)
        binding.fabAdd.setOnClickListener { navController.navigate(R.id.addExpenseFragment) }

        val topLevel = setOf(R.id.dashboardFragment, R.id.historyFragment, R.id.analyticsFragment, R.id.budgetFragment, R.id.settingsFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showAppChrome = destination.id in topLevel
            binding.bottomNav.visibility = if (showAppChrome) View.VISIBLE else View.GONE
            binding.fabAdd.visibility = if (showAppChrome) View.VISIBLE else View.GONE
        }
    }
}
