package com.rivvana.firebaseauth.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rivvana.firebaseauth.R
import com.rivvana.firebaseauth.databinding.FragmentUserBinding
import com.rivvana.firebaseauth.ui.LoginActivity
import java.io.ByteArrayOutputStream


class UserFragment : Fragment() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var _binding: FragmentUserBinding? = null
    lateinit var auth: FirebaseAuth
    private lateinit var imgUri : Uri

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
        val user = auth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("http://798514186756-8dv8n40165ae56nnfnu9s4qv0e8rjbj8.apps.googleusercontent.com/")
            .requestEmail()
            .build()

        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

        if (user != null){
            binding.etEmailUser.setText(user.email)
        }

        binding.cviUser.setOnClickListener{
            gotoCamera()
        }

        binding.btnLogout.setOnClickListener{
            logoutBtn()
        }
    }

    private fun gotoCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            intent ->
            activity?.packageManager?.let {
                intent?.resolveActivity(it).also {
                    startActivityForResult(intent, REQ_CAM)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CAM && requestCode == RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImgToFirebase(imgBitmap)
        }
    }

    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_user/${FirebaseAuth.getInstance().currentUser?.email}")
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.cviUser.setImageBitmap(imgBitmap)
                        }
                    }
                }
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

    companion object {
        const val REQ_CAM = 100
    }
}