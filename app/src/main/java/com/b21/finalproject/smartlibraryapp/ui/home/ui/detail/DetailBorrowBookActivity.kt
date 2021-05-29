package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityDetailBorrowBookBinding
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeAdapter
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailBorrowBookActivity : AppCompatActivity() {

    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: DetailViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var binding: ActivityDetailBorrowBookBinding

    companion object {
        const val IMAGE_CAPTURE = "image_capture"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBorrowBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDetailBook.toolbar)
        supportActionBar?.title = "Detail Book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        adapter = HomeAdapter()

        binding.rvWrongBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWrongBooks.setHasFixedSize(true)

        val imageCapture = intent.getParcelableExtra<Bitmap>(IMAGE_CAPTURE)
        val rotateImage = rotateImage(imageCapture!!)
        binding.layoutHeaderDetailBorrow.imgResultCapture.setImageBitmap(rotateImage)

        viewModel.getBookById(1).observe(this, { book ->
            showPopulate(book)
        })

        viewModel.getRecommendedBooks(SortUtils.RECOMMENDED).observe(this, { books ->
            adapter.setAllbooks(books)
            adapter.notifyDataSetChanged()
            binding.rvWrongBooks.adapter = adapter
        })

        binding.btnBorrow.setOnClickListener {
            val intent = Intent(this@DetailBorrowBookActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun showPopulate(book: BookEntity) {
        val url = book.imageUrl_l.split("\"", "/").toTypedArray()
        val title = book.book_title.split("\"").toTypedArray()
        val isbn = book.ISBN.split("\"").toTypedArray()
        val author = book.book_author.split("\"").toTypedArray()
        val publisher = book.publisher.split("\"").toTypedArray()

        val number2digit = Math.round(book.rating.toDouble() * 10.0) / 10.0

        val circularProgressBar = CircularProgressDrawable(this@DetailBorrowBookActivity).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        with(binding.layoutHeaderDetailBook) {
            Glide.with(this@DetailBorrowBookActivity)
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

        binding.layoutHeaderRecommended.imgItemMore.visibility = View.GONE
        binding.layoutHeaderRecommended.tvRecommendedBooks.text = "Wrong book? might you mean these book!"
    }

    private fun rotateImage(img: Bitmap): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }
}