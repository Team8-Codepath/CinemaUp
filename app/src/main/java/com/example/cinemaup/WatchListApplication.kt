package com.example.cinemaup

import android.app.Application

class WatchListApplication : Application() {
    val db by lazy {AppDatabase.getInstance(this)}
}