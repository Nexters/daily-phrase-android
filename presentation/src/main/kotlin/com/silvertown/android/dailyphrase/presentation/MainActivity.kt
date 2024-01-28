package com.silvertown.android.dailyphrase.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.silvertown.android.dailyphrase.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController =
            (supportFragmentManager.findFragmentById(R.id.fcv_nav_host) as NavHostFragment).navController
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        navController.handleDeepLink(intent)
    }
}
