package com.receparslan.artbook.apiService

import com.receparslan.artbook.BuildConfig
import com.receparslan.artbook.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtApiService {

    @GET(".")
    suspend fun getArtUrlList(
        @Query("q") q: String,
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("image_type") imageType: String = "photo",
        @Query("per_page") perPage: Int = 200
    ): Response<ApiResponse>
}
