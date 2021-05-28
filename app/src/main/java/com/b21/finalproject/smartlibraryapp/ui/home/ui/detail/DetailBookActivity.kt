package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityDetailBookBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding

    companion object {
        const val BOOK_ID = "book_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDetailBook.toolbar)
        supportActionBar?.title = "Detail Book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bookId = intent.getIntExtra(BOOK_ID, 0)

        showPopulate(bookId)
    }

    private fun showPopulate(bookId: Int) {
        with(binding) {
//            val url = bookEntity.imageUrl_l.split("\"", "/").toTypedArray()
//            val title = bookEntity.book_title.split("\"").toTypedArray()
//            val author = bookEntity.book_author.split("\"").toTypedArray()
//
//            val circularProgressBar = CircularProgressDrawable(this@DetailBookActivity).apply {
//                strokeWidth = 5f
//                centerRadius = 30f
//                start()
//            }
//
//            Glide.with()
//                .load("http://images.amazon.com/images/P/${url[6]}")
//                .apply(RequestOptions.placeholderOf(circularProgressBar).error(R.drawable.ic_error))
//                .into(imgItemBook)
        }
    }
}