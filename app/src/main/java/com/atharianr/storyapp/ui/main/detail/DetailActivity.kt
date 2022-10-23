package com.atharianr.storyapp.ui.main.detail

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.atharianr.storyapp.MyApplication
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.databinding.ActivityDetailBinding
import com.atharianr.storyapp.ui.main.MainViewModel
import com.atharianr.storyapp.utils.Constant
import com.atharianr.storyapp.utils.Constant.STORY_ID
import com.atharianr.storyapp.utils.PreferenceHelper.get
import com.atharianr.storyapp.utils.getDateFromString
import com.atharianr.storyapp.utils.toStringFormat
import com.atharianr.storyapp.utils.toast
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding as ActivityDetailBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            ibBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        getStoryDetail(intent.getStringExtra(STORY_ID) ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getStoryDetail(id: String) {
        mainViewModel.getDetailStory(
            "Bearer ${MyApplication.prefs.get(Constant.USER_TOKEN, "")}", id
        ).observe(this) {
            when (it.status) {
                StatusResponse.SUCCESS -> {
                    with(binding) {
                        it.body?.story?.apply {
                            Glide.with(this@DetailActivity).load(photoUrl).into(ivItemPhoto)
                            tvItemName.text = name
                            tvCreatedAt.text = getDateFromString(
                                createdAt, "yyyy-MM-dd'T'HH:mm:ss.SSS"
                            ).toStringFormat("EEE, d MMM yyyy HH:mm")
                            tvItemDesc.text = description
                        }
                    }
                }
                StatusResponse.ERROR -> {
                    it.message?.let { msg -> toast(this, msg) }
                }
            }
        }
    }
}