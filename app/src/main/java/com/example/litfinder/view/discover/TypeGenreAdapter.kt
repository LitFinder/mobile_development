package com.example.litfinder.view.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityContentTabGenreBinding
import com.example.litfinder.databinding.ActivityContentTabSearchBinding
import com.example.litfinder.remote.api.DataItemtype

class TypeGenreAdapter(private var genres: List<DataItemtype>, private val onGenreClick: (DataItemtype) -> Unit) : RecyclerView.Adapter<TypeGenreAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(val binding: ActivityContentTabSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityContentTabSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]
        holder.binding.contentTab.text = genre.name

        holder.binding.root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
            onGenreClick(genre)
        }

        if (position == selectedPosition) {
            holder.binding.root.setBackgroundResource(R.drawable.shape)
            holder.binding.contentTab.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.white))
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.shape3) // Default background
            holder.binding.contentTab.setTextColor(ContextCompat.getColor(holder.binding.root.context, android.R.color.black))
        }
    }

    fun setData(newGenres: List<DataItemtype>) {
        genres = newGenres
        notifyDataSetChanged()
    }
}

