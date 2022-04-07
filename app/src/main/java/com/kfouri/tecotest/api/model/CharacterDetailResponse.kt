package com.kfouri.tecotest.api.model

import com.google.gson.annotations.SerializedName

class CharacterDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("location") val location: Location,
    @SerializedName("origin") val origin: Location,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
)