package com.b21.finalproject.smartlibraryapp.ui.home.ui.books.menu.allBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.b21.finalproject.smartlibraryapp.databinding.FragmentAllBooksBinding
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeAdapter
import com.b21.finalproject.smartlibraryapp.ui.home.ui.home.HomeViewModel
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class AllBooksFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var adapter: HomeAdapter
    private var _binding: FragmentAllBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        _binding = FragmentAllBooksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeAdapter()

        binding.rvAllbooks.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvAllbooks.setHasFixedSize(true)

        homeViewModel.getAllBooks().observe(viewLifecycleOwner, { books ->
            adapter.setAllbooks(books)
            binding.rvAllbooks.adapter = adapter
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}