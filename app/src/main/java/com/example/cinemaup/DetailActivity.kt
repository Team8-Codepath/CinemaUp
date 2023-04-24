package com.example.cinemaup

import android.content.Context
import android.os.Bundle
import android.support.annotation.Keep
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.Headers


private const val TAG = "DetailActivity"

class DetailActivity: AppCompatActivity() {
    private lateinit var posterImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var client: AsyncHttpClient
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private val castDetailsList = mutableListOf<CastDetails>()
    //note: init the data we need to display

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        client = AsyncHttpClient()
        titleTextView = findViewById(R.id.movieTitle_NP)
        posterImageView = findViewById(R.id.moviePoster_NP)
        overviewTextView = findViewById(R.id.movieOverview)
        ratingTextView = findViewById(R.id.movieRating)
        releaseDateTextView = findViewById(R.id.movieRelease)
        castRecyclerView = findViewById(R.id.castDetailsRV)
        castAdapter = CastAdapter(this, castDetailsList)
        castRecyclerView.adapter = castAdapter
        castRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            castRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        //note: set equal to the Views on the activity_detail.xml file

        val movie = intent.getSerializableExtra(MOVIE_EXTRA) as Movie
        getCastMembers(movie.id)


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

    }

    private fun getCastMembers(movie_id: Int){
        val movieCreditsURL = "https://api.themoviedb.org/3/movie/${movie_id}/credits?api_key=${BuildConfig.API_KEY}&language=en-US"

        client.get(movieCreditsURL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.d(TAG, "Unable to get cast ids")

            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                val castJsonArray = json?.jsonObject?.getJSONArray("cast")
                        Log.d(TAG, "$castJsonArray")

                castJsonArray?.let {
                    for(i in 0 until castJsonArray.length()) {
                       val castDetails = createJson().decodeFromString(
                           CastDetails.serializer(),
                           castJsonArray.get(i).toString()
                       )
                        castDetails.profilePath?.let {
                            castDetailsList.add(castDetails)
                        }


                    }
                    castAdapter.notifyDataSetChanged()
                }
            }
        })
    }

}

@Keep
@Serializable
data class CastDetails(
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?
    )


class CastAdapter(private val context: Context, private val castDetailsList: List<CastDetails>) :
//note: context refers to the layout file item_movie to know where to place our data

    RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_nowplaying, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val castDetails = castDetailsList[position]
        holder.bind(castDetails)
        //note: grabs the movie at a certain position and binds its elements to the layout
    }

    override fun getItemCount() = castDetailsList.size

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

        fun bind(castDetails: CastDetails) {
            titleTextView.text = castDetails.name

//            likeIconView.visibility = View.GONE

            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/${castDetails.profilePath}")
//                .apply(RequestOptions.placeholderOf(R.mipmap.no_wifi)
                .override(300,600)
                .transform(RoundedCorners(30))
                .into(posterImageView)
        }

    }
}