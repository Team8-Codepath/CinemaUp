package com.example.cinemaup

data class DisplayWatchListMovie(
    val title: String?,
    val overview: String?,
    val poster: String?,
    val releaseDate: String?,
    val rating: Float?
) : java.io.Serializable