package com.example.cinemaup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.aallam.openai.api.BetaOpenAI
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import org.json.JSONObject

class PosterMaker : AppCompatActivity() {
    var url = "https://api.openai.com/v1/images/generations"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster_maker)
        var generateButton= findViewById<Button>(R.id.buttonGeneratePoster)
        generateButton.setOnClickListener {
            generatePoster(this)
        }
    }

    @OptIn(BetaOpenAI::class)
    private fun generatePoster(context: Context) = runBlocking {
//        val apiKey = "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"
//        val config = OpenAIConfig(
//            token = apiKey,
//            timeout = Timeout(socket = 60.seconds),
//            // additional configurations...
//        )
//        val openAI = OpenAI(config)
//        val images = openAI.imageURL( // or openAI.imageJSON
//            creation = ImageCreation(
//                prompt = "A cute baby sea otter",
//                n = 2,
//                size = ImageSize.is1024x1024
//            )
//        )
//        // Do something with the generated images...
//        updateImage(images[0].url)
        val editTextQuery =  findViewById<TextInputLayout>(R.id.editPosterMaker)

        getResponse(editTextQuery.editText?.text.toString())

    }
    private fun updateImage(url:String){
        val posterImage =  findViewById<ImageView>(R.id.posterResult)
        Glide.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade(500)) //Here a fading animation
            .transform(RoundedCorners(25)) //Here a fading animation

            .thumbnail(Glide.with(this).load(R.drawable.cinemauplogoanim))
            .timeout(250)
            .override(700,700)
            .into(posterImage)
    }
    private fun getResponse(query: String) {
        // setting text on for question on below line.
        //questionTV.text = query
        val posterImage =  findViewById<ImageView>(R.id.posterResult)

        //queryEdt.setText("")
        // creating a queue for request queue.
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        // creating a json object on below line.
        val jsonObject: JSONObject? = JSONObject()
        // adding params to json object.
        jsonObject?.put("prompt", query)
//        jsonObject?.put("Authorization", "Bearer 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")
        jsonObject?.put("n", 1)
        jsonObject?.put("size", "256x256")

        // on below line making json object request.
        val postRequest: JsonObjectRequest =
            // on below line making json object request.
            object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                Response.Listener { response ->
                    // on below line getting response message and setting it to image view.
                    var imageURL: String =
                        response.getJSONArray("data").getJSONObject(0).getString("url")
                    imageURL = imageURL.replace("\\", "");
                    // using picasso to load image from url in image view
                    updateImage(imageURL)
                   // Picasso.get().load(imageURL).into(posterImage)
                },
                // adding on error listener
                Response.ErrorListener { error ->
                    Log.e("TAGAPI", "Error is : " + error.message + "\n" + error)
                }) {
                override fun getHeaders(): kotlin.collections.MutableMap<kotlin.String, kotlin.String> {
                    val params: MutableMap<String, String> = HashMap()
                    // adding headers on below line.
                    params["Content-Type"] = "application/json"
                    params["Authorization"] =
                        "Bearer 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"
                    return params;
                }
            }
        // on below line adding retry policy for our request.
        postRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        // on below line adding our request to queue.
        queue.add(postRequest)
    }

}