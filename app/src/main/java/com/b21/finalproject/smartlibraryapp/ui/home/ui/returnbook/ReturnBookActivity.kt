package com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookWithDeadlineEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityReturnBookBinding
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class ReturnBookActivity : AppCompatActivity() {

    private lateinit var adapter: ReturnBookAdapter
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ReturnBookViewModel
    private lateinit var appPreference: AppPreference

    private lateinit var binding: ActivityReturnBookBinding

    companion object {
        private var IS_RETURN = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDetailBook.toolbar)
        supportActionBar?.title = "Return the books"

        appPreference = AppPreference(this)

        adapter = ReturnBookAdapter()
        factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ReturnBookViewModel::class.java]

        binding.rvReturnBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvReturnBooks.setHasFixedSize(true)

        viewModel.getAllborrowBooksByRaw(SortUtils.RETURN_BOOK, appPreference.userId!!).observe(this, { books ->
            if (books.isNullOrEmpty()) {
                binding.rvReturnBooks.visibility = View.GONE
                binding.tvNotif.visibility = View.VISIBLE
            } else {
                adapter.setAllbooks(books)
                binding.rvReturnBooks.adapter = adapter
                binding.rvReturnBooks.visibility = View.VISIBLE
                binding.tvNotif.visibility = View.GONE
            }
        })

        val lat = intent.getDoubleExtra("lat", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)

        if (calculateDistance(lat, long)) {
            IS_RETURN = true
            adapter.IS_RETURN = true
        } else {
            IS_RETURN = false
            adapter.IS_RETURN = false
        }

        adapter.setOnBtnReturnClickCallback(object : ReturnBookAdapter.OnBtnReturnClickCallback {
            override fun onBtnItemClickCallback(book: BookWithDeadlineEntity) {
                viewModel.updateBorrowBook(1, book.bookId)
                val intent = Intent(this@ReturnBookActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                finish()
            }
        })
    }

    private fun calculateDistance(lat2: Double, lng2: Double): Boolean {
        val lat1 = -6.1811
        val lng1 = 106.8269

        val results = FloatArray(10)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)

        val result = String.format("%.1f", results[0] / 10)

        Log.d("Location Distance new2", result)

        if (result.toDouble() > 1.5 || result.toDouble() < 0.0) {
            Log.d("Location result", "False")
            return false
        } else {
            Log.d("Location result", "True")
            return true
        }
    }
}