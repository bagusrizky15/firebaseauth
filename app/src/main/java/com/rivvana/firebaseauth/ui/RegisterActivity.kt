package com.rivvana.firebaseauth.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.rivvana.firebaseauth.R
import com.rivvana.firebaseauth.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener{
            val email = binding.etEmailRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()

            if (email.isEmpty()){
                binding.etEmailRegister.error = "Masukan Email"
                binding.etEmailRegister.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmailRegister.error = "Email salah"
                binding.etEmailRegister.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                binding.etPasswordRegister.error = "Masukan Password"
                binding.etPasswordRegister.requestFocus()
                return@setOnClickListener
            }


        }
    }
}