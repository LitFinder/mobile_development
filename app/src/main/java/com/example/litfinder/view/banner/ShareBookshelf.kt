package com.example.litfinder.view.banner

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.litfinder.databinding.ActivityShareBookshelfBinding
import com.example.litfinder.view.detailBook.DetailBook
import java.io.File
import java.io.FileOutputStream

class ShareBookshelf : AppCompatActivity() {
    private lateinit var binding: ActivityShareBookshelfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBookshelfBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val bookDetail: DataItem? = intent.getParcelableExtra(BOOK_DETAIL)

        val title = intent.getStringExtra(DetailBook.EXTRA_TITLE)
        val authors = intent.getStringExtra(DetailBook.EXTRA_AUTHORS)
        val image = intent.getStringExtra(DetailBook.EXTRA_IMAGE)
        val publisher = intent.getStringExtra(DetailBook.EXTRA_PUBLISHER)
        val description = intent.getStringExtra(DetailBook.EXTRA_DESCRIPTION)
        val previewLink = intent.getStringExtra(DetailBook.EXTRA_PREVIEW_LINK)
        val publishedDate = intent.getStringExtra(DetailBook.EXTRA_PUBLISHED_DATE)
        val infoLink = intent.getStringExtra(DetailBook.EXTRA_INFO_LINK)
        val categories = intent.getStringExtra(DetailBook.EXTRA_CATEGORIES)
        val ratingsCount = intent.getIntExtra(DetailBook.EXTRA_RATINGS_COUNT, 0)

        binding.titleContentBook.text = title
        binding.publiserContentBook.text = "Oleh : ${authors?.cleanString()}"
        binding.imgContentArticle.loadImage(image)
        binding.backToMain.setOnClickListener {
            onBackPressed()
        }
        binding.buttonShareContent.setOnClickListener{
            // screenshoot contrant layout dengan id content_book dan membagikannya ke sosialmedia yang kita bisa pilih
            val screenshot = takeScreenshot(binding.contentBook)
            shareImage(screenshot)
        }
        binding.starRating.rate(ratingsCount)
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    private fun takeScreenshot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun shareImage(bitmap: Bitmap) {
        val cachePath = File(externalCacheDir, "my_images/")
        cachePath.mkdirs()
        val file = File(cachePath, "image.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        val imageUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_AUTHORS = "EXTRA_AUTHORS"
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_PUBLISHER = "EXTRA_PUBLISHER"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_PREVIEW_LINK = "EXTRA_PREVIEW_LINK"
        const val EXTRA_PUBLISHED_DATE = "EXTRA_PUBLISHED_DATE"
        const val EXTRA_INFO_LINK = "EXTRA_INFO_LINK"
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
        const val EXTRA_RATINGS_COUNT = "EXTRA_RATINGS_COUNT"

        fun newIntent(context: Context, title: String?, authors: String?, image: String?, publisher: String?, description: String?, previewLink: String?, publishedDate: String?, infoLink: String?, categories: String?, ratingsCount: Int?): Intent {
            return Intent(context, ShareBookshelf::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_AUTHORS, authors)
                putExtra(EXTRA_IMAGE, image)
                putExtra(EXTRA_PUBLISHER, publisher)
                putExtra(EXTRA_DESCRIPTION, description)
                putExtra(EXTRA_PREVIEW_LINK, previewLink)
                putExtra(EXTRA_PUBLISHED_DATE, publishedDate)
                putExtra(EXTRA_INFO_LINK, infoLink)
                putExtra(EXTRA_CATEGORIES, categories)
                putExtra(EXTRA_RATINGS_COUNT, ratingsCount)
            }
        }
    }

    fun String.cleanString(): String {
        return this.replace("[", "")
            .replace("]", "")
            .replace("'", "")
    }
}
