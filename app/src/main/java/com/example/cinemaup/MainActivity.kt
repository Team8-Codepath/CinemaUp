package com.example.cinemaup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.cinemaup.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException


fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity"

private const val NowPlaying_MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=${BuildConfig.API_KEY}&language=en-US&page=1.json"
private const val Upcoming_MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=${BuildConfig.API_KEY}&language=en-US&page=1.json"
//    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {

    private val nowPlayingList = mutableListOf<Movie>()
    private val upcomingList = mutableListOf<Movie>()


    private lateinit var nowPlayingRV: RecyclerView
    private lateinit var upcomingRV: RecyclerView

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //note: Top Recycler View layout and binding-------------
        nowPlayingRV = findViewById(R.id.nowPlayingRV)
        val nowPlayingAdapter = MovieAdapter(this, nowPlayingList)
        nowPlayingRV.adapter = nowPlayingAdapter

        nowPlayingRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            nowPlayingRV.addItemDecoration(dividerItemDecoration)
        }

        //note: Bottom Recycler View layout and binding-------------
        upcomingRV = findViewById(R.id.upcomingRV)
        val upcomingAdapter = MovieAdapter(this, upcomingList)
        upcomingRV.adapter = upcomingAdapter

        upcomingRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            upcomingRV.addItemDecoration(dividerItemDecoration)
        }


        val client = AsyncHttpClient()

        //note: now playing recycler view API call
        client.get(NowPlaying_MOVIE_SEARCH_URL, ResponseHandler(nowPlayingList, nowPlayingAdapter))

        //note: upcoming recycler view API call
        client.get(Upcoming_MOVIE_SEARCH_URL, ResponseHandler(upcomingList, upcomingAdapter))
        Log.d(TAG, "${nowPlayingList.size}")


    }


    private class ResponseHandler(private val movieList: MutableList<Movie>, private val adapter: MovieAdapter) :
        JsonHttpResponseHandler() {
        override fun onFailure(
            statusCode: Int,
            headers: Headers?,
            response: String?,
            throwable: Throwable?
        ) {
            Log.e(TAG, "Failed to fetch movies: $statusCode")
        }

        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {

            Log.i(TAG, "Successfully fetched movies: $json")
            try {

                //note: Deserializes the given JSON string into a value of type T using the given deserializer.
                //note:public final fun <T> decodeFromString(
                //    deserializer: DeserializationStrategy<T>,
                //    string: String ): T

                //note: takes the json response and converts it to a list of movies
                val movieResponse = createJson().decodeFromString(
                    MovieResponse.serializer(),
                    json.jsonObject.toString()
                )

                //note: Collection and List are the same
                //note: this code block adds the list of movies from MovieResponse to the mutable list of movies using the addAll method
                movieResponse.movies?.let { listMovies ->
                    movieList.addAll(listMovies)
                    adapter.notifyDataSetChanged()
                }


            } catch (e: JSONException) {
                Log.e(TAG, "Exception: $e")
            }

        }

    }
}
