package com.example.litfinder.view.bookshelf

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ActivityContentBookshelfBinding
import com.example.litfinder.remote.api.DataItemBookShelf
import com.example.litfinder.view.banner.ShareBookshelf
import com.example.litfinder.view.detailBook.DetailBook

class BookselfAdapter(private var items: List<DataItemBookShelf>) :
    RecyclerView.Adapter<BookselfAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityContentBookshelfBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityContentBookshelfBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            imgContent.loadImage(item.book?.first()?.image)
            title.text = item.book?.first()?.title
            publiser.text = "oleh ${item.book?.first()?.authors?.cleanString()}"
            ulasan.text = item.rating?.reviewScore.toString()
            share.setOnClickListener {
                val context = it.context
                val intent = ShareBookshelf.newIntent(
                    context,
                    item.book?.first()?.title,
                    item.book?.first()?.authors,
                    item.book?.first()?.image,
                    item.book?.first()?.publisher,
                    item.book?.first()?.description,
                    item.book?.first()?.previewLink,
                    item.book?.first()?.publishedDate,
                    item.book?.first()?.infoLink,
                    item.book?.first()?.categories,
                    item.book?.first()?.ratingsCount
                )
                context.startActivity(intent)
            }

            item.rating?.reviewScore?.let { ratingStar.rate(it) }
            root.setOnClickListener {
                val context = it.context
                val intent = DetailBook.newIntent(
                    context,
                    item.id,
                    item.book?.first()?.title,
                    item.book?.first()?.authors,
                    item.book?.first()?.image,
                    item.book?.first()?.publisher,
                    item.book?.first()?.description,
                    item.book?.first()?.previewLink,
                    item.book?.first()?.publishedDate,
                    item.book?.first()?.infoLink,
                    item.book?.first()?.categories,
                    item.book?.first()?.ratingsCount,
                    item.status,
                    item.bookId
                )
                context.startActivity(intent)
            }
        }
    }

    fun String.cleanString(): String {
        return this.replace("[", "")
            .replace("]", "")
            .replace("'", "")
    }

    fun setData(newItems: List<DataItemBookShelf>) {
        items = newItems
        notifyDataSetChanged()
    }
}

