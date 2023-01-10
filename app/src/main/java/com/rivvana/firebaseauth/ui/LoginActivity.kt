package com.rivvana.firebaseauth.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rivvana.firebaseauth.MainActivity
import com.rivvana.firebaseauth.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("http://798514186756-8dv8n40165ae56nnfnu9s4qv0e8rjbj8.apps.googleusercontent.com/")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLoginGoogle.setOnClickListener{
            signInGoogle()
        }

        binding.btnLogin.setOnClickListener{
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            if (email.isEmpty()){
                binding.etEmailLogin.error = "Masukan Email"
                binding.etEmailLogin.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmailLogin.error = "Email salah"
                binding.etEmailLogin.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                binding.etPasswordLogin.error = "Masukan Password"
                binding.etPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            LoginFirebase(email,password)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completeTask: Task<GoogleSignInAccount>) {
        try {
            val account = completeTask.getResult(
                ApiException::class.java
            )
            if (account != null){
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener{
            task ->
                if (task.isSuccessful){
                    SavedPreference.setEmail(this, account.email.toString())
                    SavedPreference.setUsername(this, account.displayName.toString())

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
        if (GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun reload() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        val RC_SIGN_IN = 9001
    }
}