package com.b21.finalproject.smartlibraryapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.databinding.FragmentLoginBinding
import com.b21.finalproject.smartlibraryapp.databinding.FragmentRegisterBinding
import com.b21.finalproject.smartlibraryapp.ui.home.HomeActivity

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
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
            if (binding.rgSex.checkedRadioButtonId != -1) {

//                if (!binding.rbMale.isChecked)
//                    binding.rbMale.error = "Please choose your gender"
//                else if (!binding.rbFemale.isChecked)
//                    binding.rbFemale.error = "Please choose your gender"
            }
            else{
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}