package com.example.litfinder.view.discover

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ActivityContentRilisBaru2Binding
import com.example.litfinder.databinding.ActivityContentRilisBaruBinding
import com.example.litfinder.remote.api.DataItem
import com.example.litfinder.view.detailBook.DetailBook

class BookRatingsUpAdapter(private var adapterMain: List<DataItem>) :
    RecyclerView.Adapter<BookRatingsUpAdapter.ViewHolder>(){

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
            ActivityContentRilisBaru2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            publiser.text= "Oleh ${users.publisher}"
            starRating.rate(users.ratingsCount)
            root.setOnClickListener {
                val context = it.context
                val intent = DetailBook.newIntent(context, users.id, users.title, users.authors, users.image, users.publisher, users.description, users.previewLink, users.publishedDate, users.infoLink, users.categories, users.ratingsCount, "", users.id)
                context.startActivity(intent)
            }
        }
    }

    fun setData(data: List<DataItem>) {
        adapterMain = data
        notifyDataSetChanged()
    }
}

//    private var books = listOf<DataItem>()
//
//    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val title: TextView = itemView.findViewById(R.id.title_content_book)
//        val description: TextView = itemView.findViewById(R.id.publiser_content_book)
//        val image: ImageView = itemView.findViewById(R.id.img_content_book)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_content_rilis_baru, parent, false)
//        return BookViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
//        val book = books[position]
//        holder.title.text = book.title
//        holder.description.text = book.description
//        Glide.with(holder.itemView.context).load(book.image).into(holder.image)
//    }
//
//    override fun getItemCount() = books.size
//
//    fun setBooks(books: List<DataItem>) {
//        this.books = books
//        notifyDataSetChanged()
//    }




