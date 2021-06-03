package com.b21.finalproject.smartlibraryapp.ui.home.ui.history

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookWithDeadlineEntity
import com.b21.finalproject.smartlibraryapp.databinding.FragmentHistoryBinding
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var adapterBorrowed: HistoryAdapter
    private lateinit var adapterStillBorrowed: HistoryAdapter
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var appPreference: AppPreference
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val reqActivity = requireActivity() as AppCompatActivity
        reqActivity.setSupportActionBar(binding.toolbar)
        reqActivity.setTitle("")

        appPreference = AppPreference(requireContext())

        unShowLayoutHeader()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterBorrowed = HistoryAdapter()
        adapterStillBorrowed = HistoryAdapter()
        factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        binding.rvHistoryBorrowed.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHistoryBorrowed.setHasFixedSize(true)

        binding.rvHistoryStillBorrowed.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHistoryStillBorrowed.setHasFixedSize(true)

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getAllBorrowBook(appPreference.userId.toString()).observe(viewLifecycleOwner, { books ->
                    if (books != null) {
                        val borrowedBooks = ArrayList<BookWithDeadlineEntity>()
                        val stillBorrowed = ArrayList<BookWithDeadlineEntity>()
                        var isBorrowed = false
                        var isStrillBorrowed = false
                        for (book in books) {
                            if (book.returnBook == true) {
                                val borrowedBook = BookWithDeadlineEntity(
                                    book.bookId,
                                    appPreference.userId.toString(),
                                    book.ISBN,
                                    book.book_title,
                                    book.book_author,
                                    book.year_publication,
                                    book.publisher,
                                    book.imageUrl_s,
                                    book.imageUrl_m,
                                    book.imageUrl_l,
                                    book.rating,
                                    book.deadline,
                                    book.returnBook
                                )
                                borrowedBooks.add(borrowedBook)
                                isBorrowed = true
                            }
                            if (book.returnBook == false) {
                                val stillBorrow = BookWithDeadlineEntity(
                                    book.bookId,
                                    appPreference.userId.toString(),
                                    book.ISBN,
                                    book.book_title,
                                    book.book_author,
                                    book.year_publication,
                                    book.publisher,
                                    book.imageUrl_s,
                                    book.imageUrl_m,
                                    book.imageUrl_l,
                                    book.rating,
                                    book.deadline,
                                    book.returnBook
                                )
                                stillBorrowed.add(stillBorrow)
                                isStrillBorrowed = true
                            }
                        }
                        showLayoutHeader(isBorrowed, isStrillBorrowed)
                        Log.d("borrowData", borrowedBooks.toString())
                        Log.d("stillBorrowData", stillBorrowed.toString())
                        Log.d("stillBorrowData", books.toString())
                        adapterBorrowed.setAllbooks(borrowedBooks)
                        adapterStillBorrowed.setAllbooks(stillBorrowed)
                        binding.rvHistoryBorrowed.adapter = adapterBorrowed
                        binding.rvHistoryStillBorrowed.adapter = adapterStillBorrowed
                    } else {
                        unShowLayoutHeader()
                    }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_bookmark_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_bookmark_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Search your history borrowed books"
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

    private fun showLayoutHeader(isBorrowed: Boolean, isStillBorrowed: Boolean) {
        binding.tvNotif.visibility = View.GONE

        if (isBorrowed == true) {
            binding.layoutHeaderBorrowed.tvRecommendedBooks.text = "History"
            binding.layoutHeaderBorrowed.tvRecommendedBooks.visibility = View.VISIBLE
            binding.layoutHeaderBorrowed.imgItemMore.visibility = View.GONE
        }
        if (isStillBorrowed == true) {
            binding.layoutHeaderStillBorrowed.tvRecommendedBooks.text = "Still borrowed"
            binding.layoutHeaderStillBorrowed.tvRecommendedBooks.visibility = View.VISIBLE
            binding.layoutHeaderStillBorrowed.imgItemMore.visibility = View.GONE
        }

        binding.rvHistoryBorrowed.visibility = View.VISIBLE
        binding.rvHistoryStillBorrowed.visibility = View.VISIBLE
    }

    private fun unShowLayoutHeader() {
        binding.tvNotif.visibility = View.VISIBLE

        binding.layoutHeaderBorrowed.tvRecommendedBooks.text = "History"
        binding.layoutHeaderBorrowed.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderBorrowed.imgItemMore.visibility = View.GONE

        binding.layoutHeaderStillBorrowed.tvRecommendedBooks.text = "Still borrowed"
        binding.layoutHeaderStillBorrowed.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderStillBorrowed.imgItemMore.visibility = View.GONE

        binding.rvHistoryBorrowed.visibility = View.GONE
        binding.rvHistoryStillBorrowed.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}