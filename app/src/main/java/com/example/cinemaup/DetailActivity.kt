package com.example.cinemaup

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


private const val TAG = "DetailActivity"

class DetailActivity: AppCompatActivity() {
    private lateinit var posterImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var addButton: Button
    private lateinit var addedTextView: TextView
    //note: init the data we need to display


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var listOfMovieTitles = mutableListOf<String?>()

        titleTextView = findViewById(R.id.movieTitle_NP)
        posterImageView = findViewById(R.id.moviePoster_NP)
        overviewTextView = findViewById(R.id.movieOverview)
        ratingTextView = findViewById(R.id.movieRating)
        releaseDateTextView = findViewById(R.id.movieRelease)
        addButton=findViewById(R.id.movieAddButton)
        addedTextView=findViewById(R.id.addedTextView)

        addedTextView.isVisible=false
        //note: set equal to the Views on the activity_detail.xml file

        val movie = intent.getSerializableExtra(MOVIE_EXTRA) as Movie
        //note: grab the data that was sent from the MainActivity file as a Movie type object
        // which allows us to gets it's specific attributes

        //note: Set the detailed information for the movie
        titleTextView.text = movie.title
        overviewTextView.text = movie.overview
        ratingTextView.text = "Rating: "+ movie.ratingStr
        releaseDateTextView.text = "Release Date: "+ movie.releaseDate

        //note: Load the movie image
        Glide.with(this)
            .load(movie.posterUrl)
            .into(posterImageView)

        addButton.setOnClickListener {
            lifecycleScope.launch(IO) {
                //val memory = (application as LegDayApplication).db.legDayDao().getAll()
                //(application as LegDayApplication).db.legDayDao().deleteAll()
                (application as WatchListApplication).db.watchListDao().insert(
                    WatchListMovieEntity(
                        title=movie.title,
                        overview=movie.overview,
                        poster=movie.posterUrl,
                        releaseDate=movie.releaseDate,
                        rating=movie.rating
                    )
                )
            }
            addButton.isVisible=false
        }
    }
}