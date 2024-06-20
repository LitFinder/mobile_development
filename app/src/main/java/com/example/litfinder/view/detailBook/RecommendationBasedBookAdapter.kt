package com.example.litfinder.view.detailBook

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ActivityContentRilisBaruBinding
import com.example.litfinder.remote.response.FromCategoryItem

class RecommendationBasedBookAdapter(private var adapterBasedBook: List<FromCategoryItem>) :
    RecyclerView.Adapter<RecommendationBasedBookAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityContentRilisBaruBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityContentRilisBaruBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return adapterBasedBook.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = adapterBasedBook[position]

        with(holder.binding) {
            imgContentBook.loadImage(book.image)
            titleContentBook.text = book.title
            publiserContentBook.text = "Oleh ${book.publisher}"
            root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailBook::class.java).apply {
                    putExtra(DetailBook.EXTRA_ID, book.id)
                    putExtra(DetailBook.EXTRA_TITLE, book.title)
                    putExtra(DetailBook.EXTRA_AUTHORS, book.authors)
                    putExtra(DetailBook.EXTRA_IMAGE, book.image)
                    putExtra(DetailBook.EXTRA_PUBLISHER, book.publisher)
                    putExtra(DetailBook.EXTRA_DESCRIPTION, book.description)
                    putExtra(DetailBook.EXTRA_PREVIEW_LINK, book.previewLink)
                    putExtra(DetailBook.EXTRA_PUBLISHED_DATE, book.publishedDate)
                    putExtra(DetailBook.EXTRA_INFO_LINK, book.infoLink)
                    putExtra(DetailBook.EXTRA_CATEGORIES, book.categories)
                    putExtra(DetailBook.EXTRA_RATINGS_COUNT, book.ratingsCount)
                    putExtra(DetailBook.EXTRA_STATUS, "")
                    putExtra(DetailBook.EXTRA_BOOK_ID, book.id)
                    // Tambahkan flag untuk menghapus aktivitas di atas DetailBook dari tumpukan aktivitas
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                context.startActivity(intent)
            }

        }
    }


    fun setData(data: List<FromCategoryItem>) {
        adapterBasedBook = data
        notifyDataSetChanged()
    }
}
