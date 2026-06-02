package com.danish.aplikasibiodata_percobaan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController // Tambahkan import ini
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // FIX 1: Hubungkan otomatis BottomNav dengan NavController
        // Ini biar ikon tab-nya nyala otomatis pas kita pindah fragment
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.popBackStack(R.id.homeFragment, false)
                    }
                    true
                }

                R.id.studentsFragment -> {
                    if (navController.currentDestination?.id != R.id.studentsFragment) {
                        navController.navigate(R.id.studentsFragment)
                    }
                    true
                }

                R.id.scheduleFragment -> {
                    if (navController.currentDestination?.id != R.id.scheduleFragment) {
                        navController.navigate(R.id.scheduleFragment)
                    }
                    true
                }

                R.id.galleryFragment -> {
                    if (navController.currentDestination?.id != R.id.galleryFragment) {
                        navController.navigate(R.id.galleryFragment)
                    }
                    true
                }

                R.id.profileFragment -> {
                    if (navController.currentDestination?.id != R.id.profileFragment) {
                        navController.navigate(R.id.profileFragment)
                    }
                    true
                }

                else -> false
            }
        }
    }
}