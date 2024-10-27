package com.devappspros.barcodescanner.data.api

import com.devappspros.barcodescanner.data.model.covertArtArchiveResponse.DevAppsCoverArtArchiveResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoverArtArchiveService {
    @GET("release/{MBID}")
    suspend fun getCoverArt(@Path("MBID") mbid: String): DevAppsCoverArtArchiveResponse?
}