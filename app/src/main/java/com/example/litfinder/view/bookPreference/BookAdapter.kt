package com.example.litfinder.view.bookPreference

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ItemRowBookBinding
import com.example.litfinder.remote.response.BookItem

class BookAdapter(private val onSelectedIdsChanged: (Array<String>) -> Unit) :
    PagingDataAdapter<BookItem, BookAdapter.BookViewHolder>(DIFF_CALLBACK) {

    private val selectedIds = mutableListOf<String>()

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
                tvTitle.text = book.title

                val bookId = book.id

                if (selectedIds.contains(bookId)) {
                    ivChecked.visibility = View.VISIBLE
                } else {
                    ivChecked.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    if (selectedIds.contains(bookId)) {
                        selectedIds.remove(bookId)
                        ivChecked.visibility = View.GONE
                    } else {
                        selectedIds.add(bookId.toString())
                        ivChecked.visibility = View.VISIBLE
                    }
                    onSelectedIdsChanged(selectedIds.toTypedArray())
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
