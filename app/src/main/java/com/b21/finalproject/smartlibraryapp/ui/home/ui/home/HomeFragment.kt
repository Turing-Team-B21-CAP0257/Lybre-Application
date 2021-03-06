package com.b21.finalproject.smartlibraryapp.ui.home.ui.home

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.databinding.FragmentHomeBinding
import com.b21.finalproject.smartlibraryapp.ml.Model3
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.services.ModelService
import com.b21.finalproject.smartlibraryapp.ui.home.ui.books.BooksActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.detail.DetailBorrowBookActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook.ReturnBookActivity
import com.b21.finalproject.smartlibraryapp.ui.home.ui.settings.SettingsActivity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import org.checkerframework.checker.nullness.qual.NonNull
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(), CoroutineScope {

    private lateinit var factory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var recommendedAdapter: HomeAdapter
    private lateinit var resultAdapter: HomeAdapter
    private var _binding: FragmentHomeBinding? = null

    private lateinit var outputStream: OutputStream
    private lateinit var dataReceiver: BroadcastReceiver

    private lateinit var dataRecommended: ArrayList<Int>
    private lateinit var appPreference: AppPreference

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var job : Job

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var task: Task<Location>

    private lateinit var handler: Handler

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val GPS_REQUEST_CODE = 110
    }

    private var isLoadModel = false

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

        appPreference = AppPreference(requireContext())
        binding.layoutHeaderHome.tvUsername.text = appPreference.username

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unShowPopulate()

        dataRecommended = ArrayList()
        handler = Handler(Looper.getMainLooper())

        val mStartModelService = Intent(requireContext(), ModelService::class.java)
        mStartModelService.putExtra("userId", appPreference.userId)
        ModelService.enqueueWork(requireContext(), mStartModelService)

        dataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                dataRecommended = intent.getIntegerArrayListExtra("data_model")!!
                lifecycleScope.launch(Dispatchers.Main) {
                    homeViewModel.getBookRecommendedById(dataRecommended).observe(viewLifecycleOwner, { books ->
                        showRecommended()
                        Log.d("HomeActivity2", books.toString())
                        Log.d("HomeActivity2", dataRecommended.toString())
                        Log.d("HomeActivity2", "135679")
                        recommendedAdapter.setAllbooks(books)
                        binding.rvRecommendedBooks.adapter = recommendedAdapter
                    })
                }
                Log.d("HomeActivity", "computing data from model was done $dataRecommended")
            }
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(dataReceiver, IntentFilter("data"));

        //go to recommended books fragment in Books Activity
        binding.layoutHeaderRecommended.imgItemMore.setOnClickListener {
            val intent = Intent(requireContext(), BooksActivity::class.java)
            intent.putExtra(BooksActivity.GOTO_NAV, "recommended")
            startActivity(intent)
        }

        // go to books activity
        binding.layoutHeaderAllbooks.imgItemMore.setOnClickListener {
            val intent = Intent(requireContext(), BooksActivity::class.java)
            startActivity(intent)
        }

        binding.layoutHeaderHome.cardMenuBorrowed.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST_CODE)
            }
        }

        binding.layoutHeaderHome.cardMenuReturn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                task = fusedLocationProviderClient.lastLocation
                task.addOnSuccessListener {
                    if (it != null) {
                        val intent = Intent(requireContext(), ReturnBookActivity::class.java)
                        intent.putExtra("lat", it.latitude)
                        intent.putExtra("long", it.longitude)
                        startActivity(intent)
                        Log.d("Location", it.latitude.toString() + " " + it.longitude.toString())
                    }
                }
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), GPS_REQUEST_CODE)
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

            Log.d("HomeActivity3", isLoadModel.toString())

            job = launch {
                val getData = async(Dispatchers.Main) {  getDataFromViewModel() }
                getData.await()
            }

            job.start()
        }

//        loadModel3(1, 200)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(requireContext(), "You must be allow the permission for camera!", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == GPS_REQUEST_CODE && grantResults.isNotEmpty()) {
            task.addOnSuccessListener {
                if (it != null) {
                    val intent = Intent(requireContext(), ReturnBookActivity::class.java)
                    intent.putExtra("lat", it.latitude)
                    intent.putExtra("long", it.longitude)
                    startActivity(intent)
                    Log.d("Location", it.latitude.toString() + " " + it.longitude.toString())
                }
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

                val currentTime = saveImage(image)

                val filePath = context?.getExternalFilesDir("CaptureImage")
                val dir = "${filePath}/BorrowBookCapture/${currentTime}.jpg"

                Log.d("dir", dir)
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

    private fun saveImage(image: Bitmap): Long {
        // Untuk mengambil alamat storage android
        val filePath = context?.getExternalFilesDir("CaptureImage")
        // Membuat folder pada alamat filepath
        val dir = File(filePath, "/BorrowBookCapture/")
        dir.mkdir()
        // Membuat waktu sekarang
        val currentTimeMillis = System.currentTimeMillis()
        // Membuat file image
        val file = File(dir, "${currentTimeMillis}.jpg")
        outputStream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        Toast.makeText(requireContext(), "Image Save To Internal!!!", Toast.LENGTH_SHORT).show()
        outputStream.flush()
        outputStream.close()

        // Menembalikan nilai currentTimeMillis untuk dikirim ke python
        return currentTimeMillis
    }

    /**
     * Jika sdk dibawah 30 atau android versi 10 maka gunakan rotate image
     */
//    private fun rotateImage(img: Bitmap): Bitmap? {
//        val matrix = Matrix()
//        matrix.postRotate(90f)
//        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
//        img.recycle()
//        return rotatedImg
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_setting -> {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(dataReceiver);
    }

    private fun getDataFromViewModel() {
        unShowRecommended()
        homeViewModel.getAllBooks(SortUtils.RANDOM).observe(viewLifecycleOwner, { books ->
            adapter.setAllbooks(books)
            binding.rvAllbooks.adapter = adapter
        })

        showPopulate()
    }

    private fun showPopulate() {
        binding.progressBar.visibility = View.GONE
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

    private fun showRecommended() {
        binding.layoutHeaderRecommended.tvRecommendedBooks.visibility = View.VISIBLE
        binding.layoutHeaderRecommended.imgItemMore.visibility = View.VISIBLE
    }

    private fun unShowRecommended() {
        binding.layoutHeaderRecommended.tvRecommendedBooks.visibility = View.GONE
        binding.layoutHeaderRecommended.imgItemMore.visibility = View.GONE
    }

    private fun showItemSearchPopulate() {
        binding.progressBar.visibility = View.GONE
        binding.layoutHeaderResult.tvRecommendedBooks.text = "Result The Search"
        binding.layoutHeaderResult.imgItemMore.visibility = View.VISIBLE
        binding.layoutHeaderResult.tvRecommendedBooks.visibility = View.VISIBLE
        binding.rvSearchBooks.visibility = View.VISIBLE
    }

}