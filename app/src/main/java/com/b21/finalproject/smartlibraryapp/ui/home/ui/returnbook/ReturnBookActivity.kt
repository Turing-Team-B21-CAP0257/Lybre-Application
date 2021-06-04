package com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookWithDeadlineEntity
import com.b21.finalproject.smartlibraryapp.databinding.ActivityReturnBookBinding
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ReturnBookActivity : AppCompatActivity() {

    private lateinit var adapter: ReturnBookAdapter
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ReturnBookViewModel
    private lateinit var appPreference: AppPreference

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: ActivityReturnBookBinding

    companion object {
        private val GPS_REQUEST = 100
        private val GPS_RESULT = 101
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        adapter.setOnBtnReturnClickCallback(object : ReturnBookAdapter.OnBtnReturnClickCallback {
            override fun onBtnItemClickCallback(book: BookWithDeadlineEntity) {
                viewModel.updateBorrowBook(1, book.bookId)
                val intent = Intent(this@ReturnBookActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                finish()
            }
        })
    }

    private fun checkLocationPermission() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), GPS_REQUEST)
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                Log.d("Location", " ${it.latitude} ${it.longitude}")
            }
        }
    }
}