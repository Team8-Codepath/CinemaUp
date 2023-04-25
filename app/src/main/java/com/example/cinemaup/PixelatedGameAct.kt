package com.example.cinemaup

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.material.bottomsheet.BottomSheetBehavior
import jp.wasabeef.glide.transformations.BlurTransformation


import okhttp3.Headers
import org.json.JSONException
import java.security.AccessController.getContext


private const val SEARCH_API_KEY = "1752564ca5a3252561658a9621b19a2d"//BuildConfig.API_KEY

private const val NowPlaying_MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=${SEARCH_API_KEY}&language=en-US&page=1.json"



class PixelatedGameAct : AppCompatActivity() {
    private var movieMutableList = mutableListOf<Movie>()
    private lateinit var optionMovies:List<Movie>

    lateinit  var pixelatedPoster : ImageView
    var correctAnswerIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixelated_game)
         pixelatedPoster =  findViewById<ImageView>(R.id.pixelatedposter)

        getMovies()





    }
    private fun updateMovies(){
        if (movieMutableList.isNotEmpty()) {
            optionMovies = movieMutableList.shuffled().distinct().take(3)

        }

    }
    private fun updateUi(){

        if(movieMutableList.isNotEmpty()&&optionMovies.isNotEmpty()){
            optionMovies = optionMovies.shuffled()
             correctAnswerIndex = (0..2).random()
            val firstOptionButton =  findViewById<Button>(R.id.option1P)
            val secondOptionButton =  findViewById<Button>(R.id.option2p)
            val thirdOptionButton =  findViewById<Button>(R.id.option3p)
            val nextQuestion =  findViewById<Button>(R.id.skipQuestionButton)

            firstOptionButton.text = optionMovies[0].title
            secondOptionButton.text =  optionMovies[1].title
            thirdOptionButton.text =  optionMovies[2].title
            firstOptionButton.isVisible =true
            secondOptionButton.isVisible =true
            thirdOptionButton.isVisible =true
//            firstOptionButton.text = this.firstOption.title
//            secondOptionButton.text= secondOption.title
//            thirdOptionButton.text= thirdOption.title

            firstOptionButton.setOnClickListener {
                buttonFunction(firstOptionButton.text.toString())
            }
            secondOptionButton.setOnClickListener {
                buttonFunction(secondOptionButton.text.toString())
            }
            thirdOptionButton.setOnClickListener {
                buttonFunction(thirdOptionButton.text.toString())
            }
            nextQuestion.setOnClickListener {
                nextQuestionFunction()
            }

            var finalUrl = "https://image.tmdb.org/t/p/w500" +optionMovies[correctAnswerIndex].poster
        Glide.with(this)
            .load(finalUrl)
            .transition(DrawableTransitionOptions.withCrossFade(500)) //Here a fading animation

            .override(700,700).timeout(250)
            .thumbnail(Glide.with(this).load(R.drawable.cinemauplogoanim))
            .transform(BlurTransformation(150),RoundedCorners(25))
            .into(pixelatedPoster)



        }
    }
    private fun nextQuestionFunction(){
        updateMovies()
        val fragment = movieInfoFragment()

        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.popBackStack()


        updateUi()
    }
    private fun buttonFunction(title:String){
        //in the case the user guess correctly

        if(title==optionMovies[correctAnswerIndex].title){
            Log.d("dd","correct")
            //image setting
            var finalUrl = "https://image.tmdb.org/t/p/w500/" +optionMovies[correctAnswerIndex].poster
            Glide.with(this)
                .load(finalUrl)
                .transition(DrawableTransitionOptions.withCrossFade(500)) //Here a fading animation
                .transform(RoundedCorners(25)) //Here a fading animation

                .thumbnail(Glide.with(this).load(R.drawable.cinemauplogoanim))
                .timeout(250)
                .override(700,700)
                .into(pixelatedPoster)
            val screenpixelc = findViewById<ConstraintLayout>(R.id.pixelGameScreenConstraint)
            CommonConfetti.rainingConfetti(screenpixelc,intArrayOf( Color.rgb(33, 146, 255),Color.rgb(56, 229, 77),Color.rgb(156, 255, 46),Color.rgb(253, 255, 0),Color.rgb(255, 30, 30),Color.rgb(255, 198, 0)) )
                .stream(5500)

            //bottom sheet
            val fragment = movieInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable("movie", optionMovies[correctAnswerIndex])
            fragment.arguments = bundle

            val firstOptionButton =  findViewById<Button>(R.id.option1P)
            val secondOptionButton =  findViewById<Button>(R.id.option2p)
            val thirdOptionButton =  findViewById<Button>(R.id.option3p)
            firstOptionButton.isVisible =false
            secondOptionButton.isVisible =false
            thirdOptionButton.isVisible =false

            supportFragmentManager.beginTransaction()
                .replace(R.id.buttonFragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()

        }
    }

    private fun getMovies(){
        val client = AsyncHttpClient()
        client.get(NowPlaying_MOVIE_SEARCH_URL, object : JsonHttpResponseHandler() {

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("TAG", "Failed to fetch movies: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i("TAG", "Successfully fetched movies: ${json}")
                try {



                    //note: takes the json response and converts it to a list of movies
                    val movieResponse = createJson().decodeFromString(
                        MovieResponse.serializer(),
                        json.jsonObject.toString()
                    )

                    //note: Collection and List are the same
                    //note: this code block adds the list of movies from MovieResponse to the mutable list of movies using the addAll method
                    movieResponse.movies?.let { listMovies ->
                        movieMutableList.addAll(listMovies)
                        updateMovies()
                        updateUi()
                    }


                } catch (e: JSONException) {
                    Log.e("TAG", "Exception: $e")
                }
            }

        })

    }
}