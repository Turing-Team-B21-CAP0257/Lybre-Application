package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityDetailBookBinding
import com.b21.finalproject.smartlibraryapp.ui.home.ui.books.BooksActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeAdapter
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailBookActivity : AppCompatActivity() {

    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: DetailViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: ActivityDetailBookBinding

    companion object {
        const val BOOK_ID = "book_id"
        private var TAG = DetailBookActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDetailBook.toolbar)
        supportActionBar?.title = "Detail Book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = HomeAdapter()

        val bookId = intent.getIntExtra(BOOK_ID, 0)

        Log.i(TAG, bookId.toString())

        binding.rvRecommendedBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedBooks.setHasFixedSize(true)

        factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        viewModel.getBookById(bookId).observe(this, { book ->
            showPopulate(book)
        })

        viewModel.getRecommendedBooks(SortUtils.RECOMMENDED).observe(this, { books ->
            adapter.setAllbooks(books)
            binding.rvRecommendedBooks.adapter = adapter
        })
    }

    private fun showPopulate(book: BookEntity) {
        val url = book.imageUrl_l.split("\"", "/").toTypedArray()
        val title = book.book_title.split("\"").toTypedArray()
        val isbn = book.ISBN.split("\"").toTypedArray()
        val author = book.book_author.split("\"").toTypedArray()
        val publisher = book.publisher.split("\"").toTypedArray()

        val circularProgressBar = CircularProgressDrawable(this@DetailBookActivity).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        with(binding.layoutHeaderDetailBook) {
            Glide.with(this@DetailBookActivity)
                .load("http://images.amazon.com/images/P/${url[6]}")
                .apply(RequestOptions.placeholderOf(circularProgressBar).error(R.drawable.ic_error))
                .into(imgItemBook)

            tvBookTitle.text = title[1]
            tvIsbn.text = isbn[1]
            tvYearPublication.text = book.year_publication
            tvAuthor.text = author[1]
            tvPublisher.text = publisher[1]

            ratingBar.rating = book.rating.toFloat()
        }

        binding.layoutHeaderRecommended.imgItemMore.setOnClickListener {
            val intent = Intent(this@DetailBookActivity, BooksActivity::class.java)
            startActivity(intent)
        }
    }
}