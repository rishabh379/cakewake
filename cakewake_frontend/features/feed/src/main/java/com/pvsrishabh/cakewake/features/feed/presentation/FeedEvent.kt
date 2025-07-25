package com.pvsrishabh.cakewake.features.feed.presentation

sealed class FeedEvent {
    object LoadFeed : FeedEvent()
    object RefreshFeed : FeedEvent()
    data class LikePost(val postId: String) : FeedEvent()
    data class FollowUser(val userId: String) : FeedEvent()
    data class ShowComments(val postId: String) : FeedEvent()
    data class NavigateToCategory(val categoryId: String) : FeedEvent()
}