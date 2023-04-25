package com.example.cinemaup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"


class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
//note: context refers to the layout file item_movie to know where to place our data

    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_nowplaying, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        //note: grabs the movie at a certain position and binds its elements to the layout
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        private val posterImageView = itemView.findViewById<ImageView>(R.id.moviePoster_NP)
        private val titleTextView = itemView.findViewById<TextView>(R.id.movieTitle_NP)
        //note: get the views to show on the main activity

//        private val overviewTextView = itemView.findViewById<TextView>(R.id.movieOverview)
/*
        init {
            itemView.setOnClickListener(this)
        }*/

        fun bind(movie: Movie) {
            titleTextView.text = movie.title

            Glide.with(context)
                .load(movie.posterUrl)
//                .apply(RequestOptions.placeholderOf(R.mipmap.no_wifi)
                .override(300,600)
                .transform(RoundedCorners(30))
                .into(posterImageView)
            //note: just want to show the movie title and poster in the MainActivity xml

            itemView.setOnClickListener{
                val intent = Intent(context, DetailActivity::class.java)
                //note: again context is used to denote the current actviity,
                // the second param tells us which file to pass the data to

                intent.putExtra(MOVIE_EXTRA,movie)
                //note:MOVIE_EXTRA is the keyword that allows the DeatilActvity to get the data like a hash map

//                val options : ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MovieAdapter, (View(posterImageView)), "profile")

                context.startActivity(intent)
//            intent.putExtra("title", movie.title)
//            intent.putExtra("poster", movie.posterUrl)
            }
            //note^: sets a listener for the item just like we did for button
            // and when clicked sets up and sends over the movie data to DetailActivity

        }

        /*override fun onClick(v: View?) {
            // Get selected article
            val movie = movies[absoluteAdapterPosition]

            // Navigate to Details screen and pass selected article

        }*/
    }
}