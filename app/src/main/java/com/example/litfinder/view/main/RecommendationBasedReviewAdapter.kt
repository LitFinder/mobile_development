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
import com.example.litfinder.view.detailBook.DetailBook

class RecommendationBasedReviewAdapter : PagingDataAdapter<BookItem, RecommendationBasedReviewAdapter.BookViewHolder>(DIFF_CALLBACK) {

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
                bindTitle(book.title.toString())

                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailBook::class.java).apply {
                        putExtra("EXTRA_ID", book.id.toString())
                        putExtra("EXTRA_TITLE", book.title)
                        putExtra("EXTRA_AUTHORS", book.authors)
                        putExtra("EXTRA_IMAGE", book.image)
                        putExtra("EXTRA_PUBLISHER", book.publisher)
                        putExtra("EXTRA_DESCRIPTION", book.description)
                        putExtra("EXTRA_PREVIEW_LINK", book.previewLink)
                        putExtra("EXTRA_PUBLISHED_DATE", book.publishedDate)
                        putExtra("EXTRA_INFO_LINK", book.infoLink)
                        putExtra("EXTRA_CATEGORIES", book.categories)
                        putExtra("EXTRA_RATINGS_COUNT", book.ratingsCount.toString())
//                        putExtra("EXTRA_STATUS", book.status)
                        putExtra("EXTRA_BOOK_ID", book.id.toString())
                    }
                    context.startActivity(intent)
                    book.id?.let { it1 -> onBookClicked(it1) }
                }
            }
        }

        private fun bindTitle(title: String) {
            binding.tvTitle.text = title
        }
    }

    fun setOnBookClickedListener(listener: (Int) -> Unit) {
        onBookClicked = listener
    }

    private lateinit var onBookClicked: (Int) -> Unit

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
