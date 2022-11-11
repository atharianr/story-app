package com.atharianr.storyapp.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atharianr.storyapp.data.source.local.entity.StoryEntity
import com.atharianr.storyapp.databinding.ItemsStoryBinding
import com.atharianr.storyapp.utils.getDateFromString
import com.atharianr.storyapp.utils.toStringFormat
import com.bumptech.glide.Glide

class StoryPagingAdapter :
    PagingDataAdapter<StoryEntity, StoryPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    var onItemClickCallback: ((String, ActivityOptionsCompat) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(private val binding: ItemsStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryEntity) {
            with(binding) {
                Glide.with(itemView).load(data.photoUrl).centerCrop().into(ivItemPhoto)

                tvItemName.text = data.name
                tvCreatedAt.text = getDateFromString(
                    data.createdAt, "yyyy-MM-dd'T'HH:mm:ss.SSS"
                ).toStringFormat("EEE, d MMM yyyy HH:mm")
                tvItemDesc.text = data.description

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(cvItemPhoto, "cv_photo"),
                            Pair(tvItemName, "name"),
                            Pair(tvCreatedAt, "date"),
                            Pair(tvItemDesc, "desc")
                        )

                    onItemClickCallback?.invoke(data.id, optionsCompat)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}