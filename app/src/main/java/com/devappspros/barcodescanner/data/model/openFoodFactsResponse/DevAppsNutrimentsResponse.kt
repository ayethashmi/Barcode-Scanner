package com.devappspros.barcodescanner.data.model.openFoodFactsResponse

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsNutrimentsResponse(
    // ---- Energy (kj) ----

    @SerializedName("energy_100g")
    @Expose
    val energyKj100g: Number? = null,

    @SerializedName("energy_serving")
    @Expose
    val energyKjServing: Number? = null,

    @SerializedName("energy-kj_unit")
    @Expose
    val energyKjUnit: String? = null,

    // ---- Energy (kcal) ----

    @SerializedName("energy-kcal_100g")
    @Expose
    val energyKcal100g: Number? = null,

    @SerializedName("energy-kcal_serving")
    @Expose
    val energyKcalServing: Number? = null,

    @SerializedName("energy-kcal_unit")
    @Expose
    val energyKcalUnit: String? = null,

    // ---- Fat ----

    @SerializedName("fat_100g")
    @Expose
    val fat100g: Number? = null,

    @SerializedName("fat_serving")
    @Expose
    val fatServing: Number? = null,

    @SerializedName("fat_unit")
    @Expose
    val fatUnit: String? = null,

    // ---- Saturated Fat ----

    @SerializedName("saturated-fat_100g")
    @Expose
    val saturatedFat100g: Number? = null,

    @SerializedName("saturated-fat_serving")
    @Expose
    val saturatedFatServing: Number? = null,

    @SerializedName("saturated-fat_unit")
    @Expose
    val saturatedFatUnit: String? = null,

    // ---- Carbohydrates ----

    @SerializedName("carbohydrates_100g")
    @Expose
    val carbohydrates100g: Number? = null,

    @SerializedName("carbohydrates_serving")
    @Expose
    val carbohydratesServing: Number? = null,

    @SerializedName("carbohydrates_unit")
    @Expose
    val carbohydratesUnit: String? = null,

    // ---- Sugars ----

    @SerializedName("sugars_100g")
    @Expose
    val sugars100g: Number? = null,

    @SerializedName("sugars_serving")
    @Expose
    val sugarsServing: Number? = null,

    @SerializedName("sugars_unit")
    @Expose
    val sugarsUnit: String? = null,

    // ---- Starch ----

    @SerializedName("starch_100g")
    @Expose
    val starch100g: Number? = null,

    @SerializedName("starch_serving")
    @Expose
    val starchServing: Number? = null,

    @SerializedName("starch_unit")
    @Expose
    val starchUnit: String? = null,

    // ---- Fiber ----

    @SerializedName("fiber_100g")
    @Expose
    val fiber100g: Number? = null,

    @SerializedName("fiber_serving")
    @Expose
    val fiberServing: Number? = null,

    @SerializedName("fiber_unit")
    @Expose
    val fiberUnit: String? = null,

    // ---- Proteins ----

    @SerializedName("proteins_100g")
    @Expose
    val proteins100g: Number? = null,

    @SerializedName("proteins_serving")
    @Expose
    val proteinsServing: Number? = null,

    @SerializedName("proteins_unit")
    @Expose
    val proteinsUnit: String? = null,

    // ---- Salt ----

    @SerializedName("salt_100g")
    @Expose
    val salt100g: Number? = null,

    @SerializedName("salt_serving")
    @Expose
    val saltServing: Number? = null,

    @SerializedName("salt_unit")
    @Expose
    val saltUnit: String? = null,

    // ---- Sodium ----

    @SerializedName("sodium_100g")
    @Expose
    val sodium100g: Number? = null,

    @SerializedName("sodium_serving")
    @Expose
    val sodiumServing: Number? = null,

    @SerializedName("sodium_unit")
    @Expose
    val sodiumUnit: String? = null
)