package com.atharianr.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atharianr.storyapp.MyApplication.Companion.prefs
import com.atharianr.storyapp.databinding.ActivityAuthBinding
import com.atharianr.storyapp.ui.main.MainActivity
import com.atharianr.storyapp.utils.Constant
import com.atharianr.storyapp.utils.Constant.USER_TOKEN
import com.atharianr.storyapp.utils.PreferenceHelper.get

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (prefs.get(USER_TOKEN, "") != "") {
            with(Intent(this, MainActivity::class.java)) {
                startActivity(this)
                finish()
            }
        }
    }

    private fun getToken(): String? {
        val sharedPref = getSharedPreferences(Constant.USER_DATA, Context.MODE_PRIVATE)
        return sharedPref.getString(Constant.USER_TOKEN, null)
    }
}