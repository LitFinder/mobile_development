package com.example.litfinder.view.genrePreference

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.R
import com.example.litfinder.databinding.ItemButtonGenreBinding
import com.example.litfinder.remote.response.GenreItem

class GenreAdapter(private var genres: List<GenreItem>) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private val selectedGenreIds = mutableSetOf<Int>()

    fun getSelectedGenreIds(): Set<Int> {
        return selectedGenreIds
    }

    fun setData(genres: List<GenreItem>) {
        this.genres = genres
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemButtonGenreBinding.inflate(inflater, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.bind(genre)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    inner class GenreViewHolder(private val binding: ItemButtonGenreBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnGenre.setOnClickListener {
                val genre = genres[adapterPosition]
                if (selectedGenreIds.contains(genre.id ?: -1)) {
                    selectedGenreIds.remove(genre.id)
                } else {
                    if (selectedGenreIds.size < 3) {
                        selectedGenreIds.add(genre.id ?: -1)
                    } else {
                        Toast.makeText(binding.root.context, "Genre Maksimal 3", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                notifyDataSetChanged()
                Toast.makeText(binding.root.context, "Selected Genre IDs: ${selectedGenreIds.joinToString()}", Toast.LENGTH_SHORT).show()
            }
        }

        fun bind(genre: GenreItem) {
            binding.btnGenre.text = genre.name

            if (selectedGenreIds.contains(genre.id ?: -1)) {
                binding.btnGenre.setTextColor(Color.WHITE)
                binding.btnGenre.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary70))
            } else {
                binding.btnGenre.setTextColor(Color.BLACK)
                binding.btnGenre.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary20))
            }
        }
    }
}
