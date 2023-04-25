package com.example.cinemaup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

private const val TAG = "MainActivity/"

private const val SEARCH_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"//BuildConfig.API_KEY
private const val NowPlaying_MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=${SEARCH_API_KEY}&language=en-US&page=1.json"
private const val Upcoming_MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=${SEARCH_API_KEY}&language=en-US&page=1.json"
//    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class ExploreFragment : Fragment() {

    private val nowPlayingList = mutableListOf<Movie>()
    private val upcomingList = mutableListOf<Movie>()


    private lateinit var nowPlayingRV: RecyclerView
    private lateinit var upcomingRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        nowPlayingRV = view.findViewById(R.id.nowPlayingRV)
        val nowPlayingAdapter = MovieAdapter(view.context, nowPlayingList)
        nowPlayingRV.adapter = nowPlayingAdapter

        nowPlayingRV.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false).also {
            val dividerItemDecoration = DividerItemDecoration(view.context, it.orientation)
            nowPlayingRV.addItemDecoration(dividerItemDecoration)
        }

        //note: Bottom Recycler View layout and binding-------------
        upcomingRV = view.findViewById(R.id.upcomingRV)
        val upcomingAdapter = MovieAdapter(view.context, upcomingList)
        upcomingRV.adapter = upcomingAdapter

        upcomingRV.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false).also {
            val dividerItemDecoration = DividerItemDecoration(view.context, it.orientation)
            upcomingRV.addItemDecoration(dividerItemDecoration)
        }


        val client = AsyncHttpClient()

        //note: now playing recycler view API call
        client.get(NowPlaying_MOVIE_SEARCH_URL,
            ResponseHandler(nowPlayingList, nowPlayingAdapter)
        )

        //note: upcoming recycler view API call
        client.get(Upcoming_MOVIE_SEARCH_URL,
            ResponseHandler(upcomingList, upcomingAdapter)
        )
        Log.d(TAG, "${nowPlayingList.size}")
        return view
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

        override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {

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