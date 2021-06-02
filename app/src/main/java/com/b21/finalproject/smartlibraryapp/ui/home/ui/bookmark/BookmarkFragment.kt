package com.b21.finalproject.smartlibraryapp.ui.home.ui.bookmark

import android.app.SearchManager
import android.content.Context
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.databinding.FragmentBookmarkBinding
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeAdapter
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class BookmarkFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var adapter: HomeAdapter
    private var _binding: FragmentBookmarkBinding? = null

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
        val factory = ViewModelFactory.getInstance(requireContext())
        bookmarkViewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]

        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val reqActivity = requireActivity() as AppCompatActivity
        reqActivity.setSupportActionBar(binding.toolbar)
        reqActivity.setTitle("")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter()

        binding.rvFavoriteBooks.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvFavoriteBooks.setHasFixedSize(true)

        factory = ViewModelFactory.getInstance(requireContext())
        bookmarkViewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_bookmark_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_bookmark_search).actionView as SearchView

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

    override fun onResume() {
        super.onResume()
        bookmarkViewModel.getAllFavoriteBook("1").observe(viewLifecycleOwner, { books ->
            if (books.isNullOrEmpty()) {
                binding.tvNotif.visibility = View.VISIBLE
                binding.rvFavoriteBooks.visibility = View.GONE
            } else {
                adapter.setAllbooks(books)
                binding.rvFavoriteBooks.adapter = adapter
                binding.tvNotif.visibility = View.GONE
            }
        })
    }
}