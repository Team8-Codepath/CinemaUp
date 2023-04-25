package com.example.cinemaup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class GamesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_games, container, false)

        view.findViewById<Button>(R.id.pixelated_game_button).setOnClickListener {
            val intent = Intent(view.context, PixelatedGameAct::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.poster_maker_button).setOnClickListener {
            val intent = Intent(view.context, PosterMaker::class.java)
            startActivity(intent)
        }

        return view
    }

}