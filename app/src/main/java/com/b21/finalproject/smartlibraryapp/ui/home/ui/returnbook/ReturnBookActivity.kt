package com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.databinding.ActivityReturnBookBinding
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class ReturnBookActivity : AppCompatActivity() {

    private lateinit var adapter: ReturnBookAdapter
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ReturnBookViewModel

    private lateinit var binding: ActivityReturnBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDetailBook.toolbar)
        supportActionBar?.title = "Return the books"

        adapter = ReturnBookAdapter()
        factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ReturnBookViewModel::class.java]

        binding.rvReturnBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvReturnBooks.setHasFixedSize(true)

        viewModel.getAllBooks(SortUtils.RANDOM).observe(this, { books ->
            adapter.setAllbooks(books)
            binding.rvReturnBooks.adapter = adapter
        })
    }
}