package com.example.cinemaup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {
    @Query("SELECT * FROM watch_list_table")
    fun getAll(): Flow<List<WatchListMovieEntity>>

    @Query("SELECT title FROM watch_list_table")
    fun getTitlesList(): List<String?>

    @Insert
    fun insertAll(movies: List<WatchListMovieEntity>)

    @Insert
    fun insert(movie: WatchListMovieEntity)

    @Query("DELETE FROM watch_list_table")
    fun deleteAll()

    @Query("DELETE FROM watch_list_table WHERE title= :deleteTitle")
    fun deleteByTitle(deleteTitle: String?)
}