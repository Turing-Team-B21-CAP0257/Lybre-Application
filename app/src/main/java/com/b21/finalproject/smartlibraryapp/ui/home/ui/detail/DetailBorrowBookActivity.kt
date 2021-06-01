package com.b21.finalproject.smartlibraryapp.ui.home.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BorrowBookEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityDetailBorrowBookBinding
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeAdapter
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions

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

        binding.rvWrongBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWrongBooks.setHasFixedSize(true)

        val imageCapture = intent.getParcelableExtra<Bitmap>(IMAGE_CAPTURE)
//        val rotateImage = rotateImage(imageCapture!!) // Jika SDK dibawah 30 atau versi android 10
        binding.layoutHeaderDetailBorrow.imgResultCapture.setImageBitmap(imageCapture)

        viewModel.getBookById(1).observe(this, { book ->
            showPopulate(book)
        })

        viewModel.getRecommendedBooks(SortUtils.RECOMMENDED).observe(this, { books ->
            adapter.setAllbooks(books)
            adapter.notifyDataSetChanged()
            binding.rvWrongBooks.adapter = adapter
        })

        binding.btnBorrow.setOnClickListener {
            val borrowBook = BorrowBookEntity(
                0,
                "1",
                "113",
                "18-05-2021",
                "22-05-2021",
                true,
                false
            )
            viewModel.insertBorrowBook(borrowBook)
            val intent = Intent(this@DetailBorrowBookActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        val image = InputImage.fromBitmap(imageCapture, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                displayTextFromImage(visionText)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayTextFromImage(visionText: Text) {
        val blockList = visionText.textBlocks
        if (blockList.size == 0) {

        } else {
            for (block in visionText.textBlocks) {
                val blockText = block.text
                Log.d("recognization: ", blockText)
            }
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
        binding.layoutHeaderRecommended.tvRecommendedBooks.text =
            "Wrong book? might you mean these book!"
    }

    /**
     *  Jika ingin digunakan untuk dibawah SDK 30
     */
//    private fun rotateImage(img: Bitmap): Bitmap? {
//        val matrix = Matrix()
//        matrix.postRotate(90f)
//        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
//        img.recycle()
//        return rotatedImg
//    }
}