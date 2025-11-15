package com.receparslan.artbook.apiService

import com.receparslan.artbook.model.ApiResponse
import com.receparslan.artbook.util.Constant.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtApiService {
    @GET("?key=${API_KEY}&image_type=photo&per_page=200")
    suspend fun getArtUrlList(@Query("q") q: String): Response<ApiResponse>
}