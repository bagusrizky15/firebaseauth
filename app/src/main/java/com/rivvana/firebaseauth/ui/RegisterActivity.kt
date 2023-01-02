package com.rivvana.firebaseauth.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rivvana.firebaseauth.MainActivity
import com.rivvana.firebaseauth.R
import com.rivvana.firebaseauth.databinding.ActivityRegisterBinding
import kotlinx.coroutines.MainScope

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

            if (password.length < 5){
                binding.etPasswordRegister.error = "Password minimal 5 karakter"
                binding.etPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            RegisterFirebase(email,password)
        }
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}