package com.b21.finalproject.smartlibraryapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.b21.finalproject.smartlibraryapp.databinding.FragmentLoginBinding
import com.b21.finalproject.smartlibraryapp.prefs.AppPreference
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
        val appPreference = AppPreference(requireContext())

        binding.btnLogin.setOnClickListener {

            val username = binding.tilUsername.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()

            if (binding.edtUsername.text.isEmpty()) {
                binding.edtUsername.error = "Username Required"
                binding.edtUsername.requestFocus()
            } else if (binding.edtPassword.text.isEmpty()) {
                binding.edtPassword.error = "Password Required"
                binding.edtPassword.requestFocus()
            } else {
                viewModel.getUserByUsername(username).observe(viewLifecycleOwner, { user ->
                    if (user != null){
                        if (password == user.password) {
                            appPreference.isLogin = true
                            appPreference.userId = user.userId
                            appPreference.username = user.username
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(requireContext(), "Wrong password !", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "Wrong username !", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}