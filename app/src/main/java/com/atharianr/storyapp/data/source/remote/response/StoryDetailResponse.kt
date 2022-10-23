package com.atharianr.storyapp.data.source.remote.response

data class StoryDetailResponse(
    val error: Boolean = false,
    val message: String = "",
    val story: Story = Story()
)