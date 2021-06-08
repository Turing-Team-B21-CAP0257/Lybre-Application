package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.FavoriteBookEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityDetailBookBinding
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.services.ModelService
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
    private lateinit var appPreference: AppPreference
    private lateinit var binding: ActivityDetailBookBinding

    companion object {
        const val BOOK_ID = "book_id"
        private var TAG = DetailBookActivity::class.java.simpleName
    }

    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDetailBook.toolbar)
        supportActionBar?.title = "Detail Book"

        adapter = HomeAdapter()

        appPreference = AppPreference(this)

        val bookId = intent.getIntExtra(BOOK_ID, 0)

        binding.rvRecommendedBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedBooks.setHasFixedSize(true)

        factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        binding.progressBar.visibility = View.VISIBLE
        binding.rvRecommendedBooks.visibility = View.GONE

        viewModel.getFavoriteBookByBookId(bookId.toString()).observe(this, { bookFavorite ->
            if (bookFavorite == null) {
                isBookmarked = false
                binding.layoutHeaderDetailBook.tbBookmark.isChecked = false
                Log.d("isBookmarked: ", isBookmarked.toString() + " false")
            } else {
                isBookmarked = true
                binding.layoutHeaderDetailBook.tbBookmark.isChecked = true
                Log.d("isBookmarked: ", isBookmarked.toString() + " true")
            }
        })

        bookmarked()

        viewModel.getBookById(bookId).observe(this, { book ->
            if (book != null) showPopulate(book)
            else binding.progressBar.visibility = View.VISIBLE
        })

        viewModel.getRecommendedBooks(SortUtils.RECOMMENDED).observe(this, { books ->
            binding.progressBar.visibility = View.VISIBLE
            if (!books.isNullOrEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.rvRecommendedBooks.visibility = View.VISIBLE
                adapter.setAllbooks(books)
                binding.rvRecommendedBooks.adapter = adapter
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvRecommendedBooks.visibility = View.GONE
            }
        })
    }

    private fun showPopulate(book: BookEntity) {
        val url = book.imageUrl_l.split("\"", "/").toTypedArray()
        val title = book.book_title.split("\"").toTypedArray()
        val isbn = book.ISBN.split("\"").toTypedArray()
        val author = book.book_author.split("\"").toTypedArray()
        val publisher = book.publisher.split("\"").toTypedArray()

        val number2digit = Math.round(book.rating.toDouble() * 10.0) / 10.0

        Log.i(TAG, number2digit.toString())

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

            ratingBar.rating = book.rating.toFloat() / 2
            tvRatings.text = "${number2digit} / 10"
        }

        binding.layoutHeaderRecommended.imgItemMore.setOnClickListener {
            val intent = Intent(this@DetailBookActivity, BooksActivity::class.java)
            startActivity(intent)
        }

        binding.layoutHeaderDetailBook.tbBookmark.setOnCheckedChangeListener { buttonView, isChecked ->
            isBookmarked = !isBookmarked
            if (isChecked) {
                isBookmarked = false
                val favoriteBookEntity = FavoriteBookEntity(
                    0,
                    appPreference.userId.toString(),
                    book.bookId,
                    true)
                viewModel.insertFavoriteBook(favoriteBookEntity)
                Log.d("save: ", "true")
            } else {
                isBookmarked = true
                viewModel.deleteFavoriteBook(book.bookId)
                Log.d("Save: ", "false")
            }
        }
    }

    private fun bookmarked() {
        if (isBookmarked) {
            binding.layoutHeaderDetailBook.tbBookmark.isChecked = true
            isBookmarked = true
        } else {
            binding.layoutHeaderDetailBook.tbBookmark.isChecked = false
            isBookmarked = false
        }
    }
}