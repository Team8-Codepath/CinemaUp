package com.example.cinemaup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class WatchListFragment : Fragment() {
    private val watchListMovies = mutableListOf<DisplayWatchListMovie>()
    private lateinit var watchListRecyclerView: RecyclerView
    private lateinit var watchListAdapter: WatchListMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //lifecycleScope.launch stuff here
        lifecycleScope.launch {
            (activity?.application as WatchListApplication).db.watchListDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayWatchListMovie(
                        entity.title,
                        entity.overview,
                        entity.poster,
                        entity.releaseDate,
                        entity.rating
                    )
                }.also { mappedList ->
                    watchListMovies.clear()
                    watchListMovies.addAll(mappedList)
                    watchListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_watch_list, container, false)
        val layoutManager = LinearLayoutManager(context)
        watchListRecyclerView = view.findViewById(R.id.watch_list_recycler_view)
        watchListRecyclerView.layoutManager = layoutManager
        watchListRecyclerView.setHasFixedSize(true)
        watchListAdapter = WatchListMovieAdapter(watchListMovies)
        watchListRecyclerView.adapter = watchListAdapter
        return view
    }

    companion object {
        fun newInstance(): WatchListFragment {
            return WatchListFragment()
        }
    }
}