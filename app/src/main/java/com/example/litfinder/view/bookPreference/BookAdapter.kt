package com.example.litfinder.view.bookPreference

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litfinder.R
import com.example.litfinder.remote.response.BookItem

class BookAdapter(private val context: Context, private val bookList: List<BookItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return bookList.size
    }

    override fun getItem(position: Int): Any {
        return bookList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_row_book, parent, false)
        val book = bookList[position]

        val ivPhoto = view.findViewById<ImageView>(R.id.iv_photo)
//        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
//        val tvDescription = view.findViewById<TextView>(R.id.tv_description)

        // Set data buku ke komponen view
        // Misalnya:
         Glide.with(context).load(book.image).into(ivPhoto)
//         tvTitle.text = book.title
//         tvDescription.text = book.description

        return view
    }
}