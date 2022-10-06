package com.atharianr.storyapp.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atharianr.storyapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}