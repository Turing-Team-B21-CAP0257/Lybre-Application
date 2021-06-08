package com.b21.finalproject.smartlibraryapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity
import com.b21.finalproject.smartlibraryapp.databinding.FragmentLoginBinding
import com.b21.finalproject.smartlibraryapp.databinding.FragmentRegisterBinding
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            var gender = ""

            if (binding.edtUsername.text.isEmpty()) {
                binding.edtUsername.error = "Username Required"
                binding.edtUsername.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtEmail.text.isEmpty()) {
                binding.edtEmail.error = "Email Required"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtAddress.text.isEmpty()) {
                binding.edtAddress.error = "Address Required"
                binding.edtAddress.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtBirthday.text.isEmpty()) {
                binding.edtBirthday.error = "Birthday Required"
                binding.edtBirthday.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPassword.text.isEmpty()) {
                binding.edtPassword.error = "Password Required"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }

            if (binding.rgSex.checkedRadioButtonId > 0) {
                when (binding.rgSex.checkedRadioButtonId) {
                    R.id.rb_female -> {
                        gender = "female"
                    }
                    R.id.rb_male -> {
                        gender = "male"
                    } else -> return@setOnClickListener
                }
            }

            var userExist = false

            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            val address = binding.edtAddress.text.toString()
            val birthday = binding.edtBirthday.text.toString()
            val email = binding.edtEmail.text.toString()

            viewModel.getUserByUsername(username).observe(viewLifecycleOwner, { user ->
                if (user != null) {
                    if (username == user.username) {
                        Toast.makeText(requireContext(), "This account had been registered", Toast.LENGTH_SHORT).show()
                        userExist = true
                    }
                }
            })

            if (!userExist) {
                val user = UserEntity(
                    "0",
                    "loremIpsum",
                    "21",
                    username,
                    gender,
                    address,
                    birthday,
                    username,
                    email,
                    password
                )
                viewModel.insertNewUser(user)
                Toast.makeText(requireContext(), "You have been create new account, please login", Toast.LENGTH_SHORT).show()
            }

        }
    }
}