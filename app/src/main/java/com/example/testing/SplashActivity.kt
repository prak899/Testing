package com.example.testing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.testing.kotlin_activity.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val keep = true
    private val DELAY = 1250
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


//        enableEdgeToEdge()
////        setContentView(R.layout.activity_splash)
//
////        startSomeNextActivity()
//
////        var keepSplashOnScreen = true
////        val delay = 2000L
////
////        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
////        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, delay)
//
//        splashScreen.setKeepOnScreenCondition { true }
////        startSomeNextActivity()
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startSomeNextActivity() {
        Handler().postDelayed({
            val intent = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 0)
    }

    override fun onResume() {
        super.onResume()
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}