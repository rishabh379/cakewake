package com.pvsrishabh.cakewake.features.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvsrishabh.cakewake.features.feed.domain.model.CakeCategory
import com.pvsrishabh.cakewake.features.feed.domain.model.FeedPost
import com.pvsrishabh.cakewake.features.feed.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _showCommentsBottomSheet = MutableStateFlow<String?>(null)
    val showCommentsBottomSheet: StateFlow<String?> = _showCommentsBottomSheet.asStateFlow()

    init {
        loadFeed()
    }

    fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.LoadFeed -> loadFeed()
            is FeedEvent.RefreshFeed -> refreshFeed()
            is FeedEvent.LikePost -> likePost(event.postId)
            is FeedEvent.FollowUser -> followUser(event.userId)
            is FeedEvent.ShowComments -> showComments(event.postId)
            is FeedEvent.NavigateToCategory -> navigateToCategory(event.categoryId)
        }
    }

    private fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Simulate network delay for shimmer
            delay(2000)

            val dummyCategories = getDummyCakeCategories()
            val dummyPosts = getDummyFeedPosts()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                cakeCategories = dummyCategories,
                feedPosts = dummyPosts
            )
        }
    }

    private fun refreshFeed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            delay(1000) // Simulate refresh delay
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    private fun likePost(postId: String) {
        val currentPosts = _uiState.value.feedPosts.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.id == postId }

        if (postIndex != -1) {
            val currentPost = currentPosts[postIndex]
            val updatedPost = currentPost.copy(
                isLiked = !currentPost.isLiked,
                likes = if (currentPost.isLiked) currentPost.likes - 1 else currentPost.likes + 1
            )
            currentPosts[postIndex] = updatedPost

            _uiState.value = _uiState.value.copy(feedPosts = currentPosts)
        }
    }

    private fun followUser(userId: String) {
        val currentPosts = _uiState.value.feedPosts.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.user.id == userId }

        if (postIndex != -1) {
            val currentPost = currentPosts[postIndex]
            val updatedUser = currentPost.user.copy(
                isFollowing = !currentPost.user.isFollowing
            )
            currentPosts[postIndex] = currentPost.copy(user = updatedUser)

            _uiState.value = _uiState.value.copy(feedPosts = currentPosts)
        }
    }

    private fun showComments(postId: String) {
        _showCommentsBottomSheet.value = postId
    }

    fun hideComments() {
        _showCommentsBottomSheet.value = null
    }

    private fun navigateToCategory(categoryId: String) {
        // TODO: Handle navigation to category screen
    }

    private fun getDummyCakeCategories(): List<CakeCategory> {
        return listOf(
            CakeCategory(
                id = "1",
                name = "Choco Chip\nBliss",
                imageUrl = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&h=400&fit=crop",
                backgroundColor = "#8B4513"
            ),
            CakeCategory(
                id = "2",
                name = "Rosy Whip\nMagic",
                imageUrl = "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?w=400&h=400&fit=crop",
                backgroundColor = "#9370DB"
            ),
            CakeCategory(
                id = "3",
                name = "Frosted\nCherry\nBliss",
                imageUrl = "https://images.unsplash.com/photo-1486427944299-d1955d23e34d?w=400&h=400&fit=crop",
                backgroundColor = "#FF7F50"
            )
        )
    }

    private fun getDummyFeedPosts(): List<FeedPost> {
        return listOf(
            FeedPost(
                id = "1",
                user = User(
                    id = "user1",
                    name = "Aman Singh",
                    profileImageUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop",
                    orderDate = "Ordered on 12 July 2025",
                    isFollowing = false
                ),
                imageUrl = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=600&h=600&fit=crop",
                title = "A daydream Choco Cake",
                hashtags = listOf("#choco"),
                likes = 8400,
                comments = 128,
                shares = 1400,
                timeAgo = "2h ago",
                isLiked = false
            ),
            FeedPost(
                id = "2",
                user = User(
                    id = "user2",
                    name = "Priya Sharma",
                    profileImageUrl = "https://images.unsplash.com/photo-1494790108755-2616c2e5d30c?w=400&h=400&fit=crop",
                    orderDate = "Ordered on 11 July 2025",
                    isFollowing = true
                ),
                imageUrl = "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?w=600&h=600&fit=crop",
                title = "Heavenly Vanilla Delight",
                hashtags = listOf("#vanilla", "#sweet"),
                likes = 6200,
                comments = 89,
                shares = 920,
                timeAgo = "4h ago",
                isLiked = true
            ),
            FeedPost(
                id = "3",
                user = User(
                    id = "user3",
                    name = "Raj Patel",
                    profileImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop",
                    orderDate = "Ordered on 10 July 2025",
                    isFollowing = false
                ),
                imageUrl = "https://images.unsplash.com/photo-1486427944299-d1955d23e34d?w=600&h=600&fit=crop",
                title = "Fresh Strawberry Paradise",
                hashtags = listOf("#strawberry", "#fresh"),
                likes = 5800,
                comments = 156,
                shares = 780,
                timeAgo = "6h ago",
                isLiked = false
            )
        )
    }
}