package com.example.litfinder.view.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ActivityContentRilisBaru2Binding
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.view.detailBook.DetailBook

class BookRatingsUpAdapter(private var adapterMain: List<BookItem>) :
    RecyclerView.Adapter<BookRatingsUpAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityContentRilisBaru2Binding) :
        RecyclerView.ViewHolder(binding.root)

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityContentRilisBaru2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return adapterMain.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = adapterMain[position]

        with(holder.binding) {
            imgContent.loadImage(users.image)
            title.text = users.title
            keteranganData.text = "( ${users.ratingsCount.toString()} )"
            publiser.text = "Oleh ${users.publisher}"
            starRating.rate(users.ratingsCount!!)
            root.setOnClickListener {
                val context = it.context
                val intent = DetailBook.newIntent(
                    context,
                    users.id,
                    users.title,
                    users.authors,
                    users.image,
                    users.publisher,
                    users.description,
                    users.previewLink,
                    users.publishedDate,
                    users.infoLink,
                    users.categories,
                    users.ratingsCount,
                    "",
                    users.id
                )
                context.startActivity(intent)
            }
        }
    }

    fun setData(data: List<BookItem>) {
        adapterMain = data
        notifyDataSetChanged()
    }
}