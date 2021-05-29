package com.b21.finalproject.smartlibraryapp.ui.home.ui.home

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.databinding.FragmentHomeBinding
import com.b21.finalproject.smartlibraryapp.ui.home.ui.books.BooksActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.detail.DetailBorrowBookActivity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(), CoroutineScope {

    private lateinit var factory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var recommendedAdapter: HomeAdapter
    private lateinit var resultAdapter: HomeAdapter
    private var _binding: FragmentHomeBinding? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var job : Job

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val CAMERA_PERMISSION_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        job = Job()
        job.start()
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

        unShowPopulate()

        //go to recommended books fragment in Books Activity
        binding.layoutHeaderRecommended.imgItemMore.setOnClickListener {
            val intent = Intent(requireContext(), BooksActivity::class.java)
            startActivity(intent)
        }

        // go to books activity
        binding.layoutHeaderAllbooks.imgItemMore.setOnClickListener {
            val intent = Intent(requireContext(), BooksActivity::class.java)
            startActivity(intent)
        }

        binding.layoutHeaderHome.cardMenuBorrowed.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        }

        if (activity != null) {
            adapter             = HomeAdapter()
            recommendedAdapter  = HomeAdapter()
            resultAdapter       = HomeAdapter()

            binding.rvRecommendedBooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvRecommendedBooks.setHasFixedSize(true)

            binding.rvAllbooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvAllbooks.setHasFixedSize(true)

            binding.rvSearchBooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvSearchBooks.setHasFixedSize(true)

            job = launch {
                val getData = async(Dispatchers.Main) {  getDataFromViewModel() }
                getData.await()
            }
            job.start()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(requireContext(), "You must allow the permission for camera !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val image: Bitmap = data?.extras?.get("data") as Bitmap
                Log.i("MYTAG", image.toString())
                val intent = Intent(requireContext(), DetailBorrowBookActivity::class.java)
                intent.putExtra(DetailBorrowBookActivity.IMAGE_CAPTURE, image)
                startActivity(intent)
            }
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
                val text = query as String
                homeViewModel.getBookByQuery(text).observe(viewLifecycleOwner, { books ->
                    showItemSearchPopulate()
                    resultAdapter.setAllbooks(books)
                    binding.rvSearchBooks.adapter = resultAdapter
                })
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
        job.cancel()
    }

    private fun getDataFromViewModel() {
        homeViewModel.getAllBooks(SortUtils.RANDOM).observe(viewLifecycleOwner, { books ->
            adapter.setAllbooks(books)
            binding.rvAllbooks.adapter = adapter
        })

        homeViewModel.getRecommendedBooks(SortUtils.RECOMMENDED).observe(viewLifecycleOwner, { books ->
            showPopulate()
            recommendedAdapter.setAllbooks(books)
            binding.rvRecommendedBooks.adapter = recommendedAdapter
        })
    }

    private fun showPopulate() {
        binding.progressBar.visibility = View.GONE
        binding.layoutHeaderRecommended.tvRecommendedBooks.visibility = View.VISIBLE
        binding.layoutHeaderRecommended.imgItemMore.visibility = View.VISIBLE
        binding.layoutHeaderAllbooks.tvAllbooks.visibility = View.VISIBLE
        binding.layoutHeaderAllbooks.imgItemMore.visibility = View.VISIBLE
        binding.layoutHeaderResult.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderResult.imgItemMore.visibility = View.GONE
    }

    private fun unShowPopulate() {
        binding.progressBar.visibility = View.VISIBLE
        binding.layoutHeaderRecommended.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderRecommended.imgItemMore.visibility = View.GONE
        binding.layoutHeaderAllbooks.tvAllbooks.visibility = View.GONE
        binding.layoutHeaderAllbooks.imgItemMore.visibility = View.GONE
        binding.layoutHeaderResult.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderResult.imgItemMore.visibility = View.GONE
    }

    private fun showItemSearchPopulate() {
        binding.progressBar.visibility = View.GONE
        binding.layoutHeaderResult.tvRecommendedBooks.text = "Result The Search"
        binding.layoutHeaderResult.imgItemMore.visibility = View.VISIBLE
        binding.layoutHeaderResult.tvRecommendedBooks.visibility = View.VISIBLE
        binding.rvSearchBooks.visibility = View.VISIBLE
    }

    private fun unShowItemSearchPopulate() {
        binding.progressBar.visibility = View.GONE
        binding.rvAllbooks.visibility = View.GONE
        binding.rvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderRecommended.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderRecommended.imgItemMore.visibility = View.GONE
        binding.layoutHeaderRecommended.tvRecommendedBooks.text = "Result The Search"
        binding.layoutHeaderAllbooks.tvAllbooks.visibility = View.GONE
        binding.layoutHeaderAllbooks.imgItemMore.visibility = View.GONE
        binding.rvSearchBooks.visibility = View.GONE
    }

}