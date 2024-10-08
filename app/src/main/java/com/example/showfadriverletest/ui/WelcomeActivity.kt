package com.example.showfadriverletest.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.showfadriverletest.R
import com.example.showfadriverletest.databinding.ActivityWelcomeBinding
import com.example.showfadriverletest.ui.login.LoginActivity
import com.example.showfadriverletest.ui.register.RegisterOtpActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this@WelcomeActivity, callback)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            }
            tvRegister.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, RegisterOtpActivity::class.java))
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            }
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            finishAffinity()
        }
    }
}