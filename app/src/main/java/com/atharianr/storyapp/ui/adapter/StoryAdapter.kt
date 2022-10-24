package com.atharianr.storyapp.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.atharianr.storyapp.data.source.remote.response.Story
import com.atharianr.storyapp.databinding.ItemsStoryBinding
import com.atharianr.storyapp.utils.getDateFromString
import com.atharianr.storyapp.utils.toStringFormat
import com.bumptech.glide.Glide

class StoryAdapter(private val callback: (String, ActivityOptionsCompat) -> Unit) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private var listData = ArrayList<Story>()

    fun setData(data: List<Story>) {
        this.listData.clear()
        this.listData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ItemsStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Story) {
            binding.apply {
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

                    callback.invoke(data.id, optionsCompat)
                }
            }
        }
    }
}