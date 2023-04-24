package com.example.cinemaup


import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep

@Serializable

data class MovieResponse (
    @SerialName("results")

    val movies: List<Movie>?
    )

@Keep
@Serializable

data class Movie(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String?,

    @SerialName("overview")
    val overview: String?,

    @SerialName("poster_path")
    val poster: String?,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("vote_average")
    val rating: Float?,

    var isLiked: Boolean = false
) : java.io.Serializable
{
    val posterUrl =
        "https://image.tmdb.org/t/p/w500/${poster}"

    val ratingStr = rating.toString()

//    var isLiked = false
}
