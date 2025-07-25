package com.pvsrishabh.cakewake.features.feed.domain.model

data class FeedPost(
    val id: String,
    val user: User,
    val imageUrl: String,
    val title: String,
    val hashtags: List<String>,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val timeAgo: String,
    val isLiked: Boolean = false
)