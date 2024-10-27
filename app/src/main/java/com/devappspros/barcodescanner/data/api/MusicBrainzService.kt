package com.devappspros.barcodescanner.data.api

import com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumInfo.DevAppsMusicAlbumInfoResponse
import com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumTracks.DevAppsMusicAlbumTracksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicBrainzService {
    @GET("release/")
    suspend fun getAlbumInfoFromBarcode(@Query("query") barcode: String, @Query("fmt") fmt: String = "json"): DevAppsMusicAlbumInfoResponse?

    @GET("release/{DISC_ID}")
    suspend fun getAlbumTracks(@Path("DISC_ID") discId: String, @Query("inc") inc: String = "recordings", @Query("fmt") fmt: String = "json"): DevAppsMusicAlbumTracksResponse?
}