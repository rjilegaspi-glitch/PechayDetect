package com.jp.pechaydetect.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jp.pechaydetect.MainActivity
import com.jp.pechaydetect.databinding.ActivitySplashBinding
import com.jp.pechaydetect.ui.onboarding.OnboardingActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val PREF_NAME = "pechay_prefs"
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
        private const val SPLASH_DELAY_MS = 2200L
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
            val nextActivity: Class<*> = if (onboardingDone) {
                MainActivity::class.java
            } else {
                OnboardingActivity::class.java
            }
            startActivity(Intent(this@SplashActivity, nextActivity))
            finish()
        }
    }
}
