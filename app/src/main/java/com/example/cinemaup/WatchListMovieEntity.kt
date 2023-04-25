package com.example.cinemaup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_list_table")
data class WatchListMovieEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name="title") val title: String?,
    @ColumnInfo(name="overview") val overview: String?,
    @ColumnInfo(name="poster") val poster: String?,
    @ColumnInfo(name="releaseDate") val releaseDate: String?,
    @ColumnInfo(name="rating") val rating: Float?
    )

