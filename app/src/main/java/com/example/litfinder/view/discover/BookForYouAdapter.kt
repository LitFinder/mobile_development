package com.example.litfinder.view.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ActivityContentRilisBaruBinding
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.view.detailBook.DetailBook

class BookForYouAdapter(private var adapterBookForYou: List<BookItem>) :
    RecyclerView.Adapter<BookForYouAdapter.ViewHolder>() {

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
        return adapterBookForYou.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = adapterBookForYou[position]

        with(holder.binding) {
            imgContentBook.loadImage(users.image)
            titleContentBook.text = users.title
            publiserContentBook.text = "Oleh ${users.publisher}"
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
        adapterBookForYou = data
        notifyDataSetChanged()
    }
}