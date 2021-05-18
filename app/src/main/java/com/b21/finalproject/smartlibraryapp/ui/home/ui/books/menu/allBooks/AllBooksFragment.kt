package com.b21.finalproject.smartlibraryapp.ui.home.ui.books.menu.allBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.b21.finalproject.smartlibraryapp.databinding.FragmentAllBooksBinding

class AllBooksFragment : Fragment() {

    private lateinit var homeViewModel: AllBooksViewModel
    private var _binding: FragmentAllBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(AllBooksViewModel::class.java)

        _binding = FragmentAllBooksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAllBooks
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}