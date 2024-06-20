package com.example.litfinder.view.detailBook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.databinding.ActivityContentUlasanBinding
import com.example.litfinder.remote.api.DataItemRating

class RatingAdapter(private var ratings: List<DataItemRating?>?) :
    RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityContentUlasanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityContentUlasanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ratings?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rating = ratings?.get(position)
        holder.binding.nameReviews.text = rating?.profileName
        holder.binding.diskripsiReviews.text = rating?.reviewSummary
        holder.binding.ratingReviews.rate(rating?.reviewScore!!)
    }

    fun setData(newRatings: List<DataItemRating?>?) {
        ratings = newRatings
        notifyDataSetChanged()
    }
}
