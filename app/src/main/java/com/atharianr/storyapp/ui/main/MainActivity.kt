package com.atharianr.storyapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.atharianr.storyapp.MyApplication.Companion.prefs
import com.atharianr.storyapp.R
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.databinding.ActivityMainBinding
import com.atharianr.storyapp.ui.adapter.LoadingStateAdapter
import com.atharianr.storyapp.ui.adapter.StoryPagingAdapter
import com.atharianr.storyapp.ui.auth.AuthActivity
import com.atharianr.storyapp.ui.main.add_story.AddStoryActivity
import com.atharianr.storyapp.ui.main.detail.DetailActivity
import com.atharianr.storyapp.ui.main.maps.MapsActivity
import com.atharianr.storyapp.utils.Constant.STORY_ID
import com.atharianr.storyapp.utils.Constant.USER_NAME
import com.atharianr.storyapp.utils.PreferenceHelper.clearSession
import com.atharianr.storyapp.utils.PreferenceHelper.get
import com.atharianr.storyapp.utils.gone
import com.atharianr.storyapp.utils.toast
import com.atharianr.storyapp.utils.visible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var storyPagingAdapter: StoryPagingAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyPagingAdapter = StoryPagingAdapter().apply {
            onItemClickCallback = { id, opt ->
                startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra(STORY_ID, id)
                }, opt.toBundle())
            }
        }

        with(binding) {
            tvGreetings.text = getString(R.string.greeting) + prefs.get(USER_NAME, "")
            fabAddStory.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }
            ibMaps.setOnClickListener {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            }
            ibLogout.setOnClickListener { logout() }
        }

        isLoading(true)
        setupRecyclerView()
        getAllStoriesPaging()
    }

    override fun onResume() {
        super.onResume()
        getAllStoriesPaging()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getAllStoriesPaging() {
        mainViewModel.getAllStoriesPaging().observe(this) {
            storyPagingAdapter.submitData(lifecycle, it)
            if (storyPagingAdapter.itemCount > 0) {
                showError(false)
            } else {
                showError(true)
            }
            isLoading(false)
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
            adapter = storyPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyPagingAdapter.retry()
                }
            )
        }
    }

    private fun isLoading(loading: Boolean) {
        binding.apply {
            if (loading) {
                rvStory.gone()
                progressBar.visible()
            } else {
                rvStory.visible()
                progressBar.gone()
            }
        }
    }

    private fun showError(error: Boolean) {
        binding.apply {
            if (error) {
                llError.visible()
                rvStory.gone()
            } else {
                llError.gone()
                rvStory.visible()
            }
        }
    }
}