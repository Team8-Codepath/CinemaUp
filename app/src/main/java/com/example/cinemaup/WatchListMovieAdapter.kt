package com.example.cinemaup

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.NonDisposableHandle.parent

class WatchListMovieAdapter (private val movies: List<DisplayWatchListMovie>) : RecyclerView.Adapter<WatchListMovieAdapter.ViewHolder>(){

    class ViewHolder(val movieView: View) : RecyclerView.ViewHolder(movieView) {

        // TODO: Create member variables for any view that will be set
        // as you render a row.
        val poster: ImageView = movieView.findViewById(R.id.watchListItemImage)
        val title: TextView = movieView.findViewById(R.id.watchListItemTitle)
        val overview: TextView = movieView.findViewById(R.id.watchListItemOverview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.watch_list_list_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val item = movies.get(position)
        // Set item views based on views and data model
        holder.title.text = item.title
        holder.overview.text = item.overview

        Glide.with(holder.movieView)
            .load(item.poster)
            .fitCenter()
            .into(holder.poster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}