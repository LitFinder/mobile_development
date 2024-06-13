package com.example.litfinder.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ItemRowBookBinding
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.view.genrePreference.GenrePreferenceActivity

class BookBerandaAdapter : PagingDataAdapter<BookItem, BookBerandaAdapter.BookViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookItem>() {
            override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class BookViewHolder(val binding: ItemRowBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookItem) {
            with(binding) {
                Glide.with(itemView.context).load(book.image).into(ivPhoto)

                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, GenrePreferenceActivity::class.java)
                    intent.putExtra("BOOK_ID", book.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemRowBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        book?.let {
            holder.bind(it)
        }
    }
}
