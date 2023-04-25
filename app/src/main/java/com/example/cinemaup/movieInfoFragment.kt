package com.example.cinemaup

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [movieInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class movieInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var movie: Movie

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getSerializable("movie") as Movie
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)
        var movieTitle =  view.findViewById<TextView>(R.id.movietitlegame)
        var description =  view.findViewById<TextView>(R.id.descriptioncontainergame)
        var rating =  view.findViewById<TextView>(R.id.ratingcontainergame)

        movieTitle.text =  movie.title
        description.text =  movie.overview
        rating.text =  movie.ratingStr
        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Movie) =
            movieInfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)

                }
            }
    }
}