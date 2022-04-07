package com.kfouri.tecotest.api.model

import com.google.gson.annotations.SerializedName

class CharacterResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<CharacterModel>,
)

class Info(
    @SerializedName("pages") val pages: Long,
)

class CharacterModel(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("location") val location: Location,
    @SerializedName("image") val image: String,
)

class Location(
    @SerializedName("name") val name: String,
)