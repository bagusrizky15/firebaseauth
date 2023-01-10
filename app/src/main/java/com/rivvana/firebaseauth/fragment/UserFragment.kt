package com.rivvana.firebaseauth.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rivvana.firebaseauth.R
import com.rivvana.firebaseauth.databinding.FragmentUserBinding
import com.rivvana.firebaseauth.ui.LoginActivity

class UserFragment : Fragment() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var _binding: FragmentUserBinding? = null
    lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("http://798514186756-8dv8n40165ae56nnfnu9s4qv0e8rjbj8.apps.googleusercontent.com/")
            .requestEmail()
            .build()

        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

        if (user != null){
            binding.etEmailUser.setText(user.email)
        }

        binding.btnLogout.setOnClickListener{
            logoutBtn()
        }
    }

    private fun logoutBtn() {
        mGoogleSignInClient.signOut().addOnCompleteListener{
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        startActivity(Intent(context, LoginActivity::class.java))
        activity?.finish()
    }
}