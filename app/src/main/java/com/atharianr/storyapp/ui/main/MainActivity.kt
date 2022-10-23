package com.atharianr.storyapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.atharianr.storyapp.MyApplication.Companion.prefs
import com.atharianr.storyapp.R
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.databinding.ActivityMainBinding
import com.atharianr.storyapp.ui.adapter.StoryAdapter
import com.atharianr.storyapp.ui.auth.AuthActivity
import com.atharianr.storyapp.ui.main.add_story.AddStoryActivity
import com.atharianr.storyapp.ui.main.detail.DetailActivity
import com.atharianr.storyapp.utils.Constant.STORY_ID
import com.atharianr.storyapp.utils.Constant.USER_NAME
import com.atharianr.storyapp.utils.Constant.USER_TOKEN
import com.atharianr.storyapp.utils.PreferenceHelper.clearSession
import com.atharianr.storyapp.utils.PreferenceHelper.get
import com.atharianr.storyapp.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var storyAdapter: StoryAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter { id ->
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra(STORY_ID, id)
            })
        }

        with(binding) {
            tvGreetings.text = getString(R.string.greeting) + prefs.get(USER_NAME, "")
            fabAddStory.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity, AddStoryActivity::class.java
                    )
                )
            }
            ibLogout.setOnClickListener { logout() }
        }

        setupRecyclerView()
        getAllStories()
    }

    override fun onResume() {
        super.onResume()
        getAllStories()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getAllStories() {
        mainViewModel.getAllStories("Bearer ${prefs.get(USER_TOKEN, "")}").observe(this) {
            when (it.status) {
                StatusResponse.SUCCESS -> {
                    it.body?.apply {
                        storyAdapter.setData(this.listStory)
                    }
                }
                StatusResponse.ERROR -> {
                    it.message?.let { msg -> toast(this, msg) }
                }
            }
        }
    }

    private fun logout() {
        prefs.clearSession()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun setupRecyclerView() {
        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }
}