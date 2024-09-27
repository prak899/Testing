package com.example.testing

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            );
        }
        super.onWindowFocusChanged(hasFocus)
    }
}
