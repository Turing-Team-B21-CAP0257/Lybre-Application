package com.b21.finalproject.smartlibraryapp.ui.home.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.b21.finalproject.smartlibraryapp.databinding.FragmentProfileBinding
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel
    private lateinit var appPreference: AppPreference
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val reqActivity = requireActivity() as AppCompatActivity
        reqActivity.setSupportActionBar(binding.layoutHeaderProfile.toolbar)
        reqActivity.setTitle("")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        appPreference = AppPreference(requireContext())

        viewModel.getUserByUsername(appPreference.username.toString()).observe(viewLifecycleOwner, { user ->
            binding.layoutHeaderProfile.tvFullnameProfile.text = user.full_name
            binding.layoutAddressEditProfile.tvAddress.text = user.address
            binding.layoutBirthdayEditProfile.tvBirthday.text = user.birthday
            binding.layoutEmailEditProfile.tvEmail.text = user.email
            binding.layoutSexEditProfile.tvSex.text = user.sex

            val password = user.password.take(5) + "xxxx"
            binding.layoutPasswordEditProfile.tvPassword.text = password
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}