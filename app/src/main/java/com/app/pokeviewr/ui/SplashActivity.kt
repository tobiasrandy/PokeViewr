package com.app.pokeviewr.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var handler: Handler
    private var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500)

        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}