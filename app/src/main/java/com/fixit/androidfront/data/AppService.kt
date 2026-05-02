package com.fixit.androidfront.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("job-posts")
    suspend fun getJobPosts(
        @Query("authorRole") authorRole: String? = null,
        @Query("authorId") authorId: String? = null,
        @Query("categoryId") categoryId: String? = null,
        @Query("search") search: String? = null
    ): Response<List<JobPost>>

    @GET("job-posts/{id}")
    suspend fun getJobPostById(@Path("id") id: String): Response<JobPost>

    @GET("ads/highlighted")
    suspend fun getHighlightedAds(
        @Query("authorRole") authorRole: String? = null
    ): Response<List<Ad>>

    @GET("users/profile")
    suspend fun getProfile(): Response<ProfileResponse>
}
