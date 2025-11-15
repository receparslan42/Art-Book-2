package com.receparslan.artbook.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("hits")
    val imageUrls: List<ImageUrls>,
)

data class ImageUrls(
    val previewURL: String,
    val webformatURL: String,
)