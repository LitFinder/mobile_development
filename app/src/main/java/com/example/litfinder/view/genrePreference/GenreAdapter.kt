package com.example.litfinder.view.genrePreference

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.R

class GenreAdapter(private var genreNames: List<String>) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    fun updateGenreList(newGenreNames: List<String>) {
        genreNames = newGenreNames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_button_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genreName = genreNames[position]
        holder.bind(genreName)
    }

    override fun getItemCount(): Int {
        return genreNames.size
    }

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val genreNameTextView: TextView = itemView.findViewById(R.id.btnGenre)
        private var isClicked = false

        init {
            genreNameTextView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isClicked = !isClicked
                        val color = if (isClicked) R.color.colorSecondary else R.color.colorPrimary
                        genreNameTextView.setBackgroundResource(color)
                    }
                }
                true
            }
        }

        fun bind(genreName: String) {
            genreNameTextView.text = genreName
        }
    }
}