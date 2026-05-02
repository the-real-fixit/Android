package com.fixit.androidfront.data

import com.google.gson.annotations.SerializedName

data class Category(
    val id: String,
    val name: String,
    val iconUrl: String? = null,
    val jobCount: Int? = 0
)

data class JobPost(
    val id: String,
    val title: String,
    val description: String,
    val budget: Double? = null,
    val location: String? = null,
    val department: String? = null,
    val municipality: String? = null,
    val createdAt: String,
    val author: AuthorInfo,
    val category: CategoryInfo? = null,
    val photos: List<String> = emptyList()
)

data class AuthorInfo(
    val id: String,
    val name: String,
    val profile: UserProfileInfo? = null
)

data class CategoryInfo(
    val name: String
)

data class UserProfileInfo(
    val photoUrl: String? = null,
    val rating: Double? = 0.0,
    val jobsCompleted: Int? = 0,
    val hours: Int? = 0,
    val bio: String? = null,
    val department: String? = null,
    val municipality: String? = null,
    val role: String? = null
)

data class Ad(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val type: String, // 'JOB' | 'PROFILE' | 'PROMOTED'
    val targetId: String,
    val isPromoted: Boolean,
    val categoryName: String? = null,
    val authorRole: String? = null
)

data class ProfileResponse(
    val profile: UserProfileInfo,
    val name: String,
    val email: String,
    val role: String
)
