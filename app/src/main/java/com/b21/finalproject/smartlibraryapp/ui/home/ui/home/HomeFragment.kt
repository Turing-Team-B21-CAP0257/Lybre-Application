package com.b21.finalproject.smartlibraryapp.ui.home.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.databinding.FragmentHomeBinding
import com.b21.finalproject.smartlibraryapp.ui.home.ui.books.BooksActivity
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment() {


    private lateinit var factory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val reqActivity = requireActivity() as AppCompatActivity
        reqActivity.setSupportActionBar(binding.layoutHeaderHome.homeToolbar)
        reqActivity.setTitle("")

        val textView: TextView = binding.layoutHeaderHome.tvUsername
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // go to books activity
        binding.layoutHeaderAllbooks.imgItemMore.setOnClickListener {
            val intent = Intent(requireContext(), BooksActivity::class.java)
            startActivity(intent)
        }

        if (activity != null) {
            val allBooksAdapter = HomeAdapter()

            binding.rvRecommendedBooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvRecommendedBooks.setHasFixedSize(true)

            binding.rvAllbooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvAllbooks.setHasFixedSize(true)

            homeViewModel.getAllBooks().observe(viewLifecycleOwner, { books ->
                allBooksAdapter.setAllbooks(books)
                binding.rvAllbooks.adapter = allBooksAdapter
                binding.rvRecommendedBooks.adapter = allBooksAdapter
            })


        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_home_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Search your favorite books"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}