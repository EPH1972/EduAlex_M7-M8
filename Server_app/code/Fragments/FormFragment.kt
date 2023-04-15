package com.example.m8uf2_server.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.m8uf2_server.Classes.Producto
import com.example.m8uf2_server.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * A simple [Fragment] subclass.
 * Use the [FormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FormFragment(private val context: Context) : Fragment() {
    private val EXTERNAL_PERMISSION_REQUEST_CODE = 1002
    private var imageView: ImageView? = null
    private var photoUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_form, container, false)

        val db = Firebase.database

        val storageRef = Firebase.storage.reference

        val productRef = storageRef.child("productImg")



        val typeProduct: EditText = v.findViewById(R.id.pcType)
        val textNom:  EditText = v.findViewById(R.id.pcName)
        val numPrice: EditText = v.findViewById(R.id.pcPrice)
        val textDescription: EditText = v.findViewById(R.id.pcDescription)
        val saveButton: Button = v.findViewById(R.id.saveButton)
        val galleryButton: Button = v.findViewById(R.id.galleryButton)
        val uploadButton: Button = v.findViewById(R.id.uploadButton)

        saveButton.setOnClickListener {
            val newItem = Producto(typeProduct.text.toString(), textNom.text.toString(), numPrice.text.toString(), textDescription.text.toString(), null, false, false)
            db.getReference("Products").push().setValue(newItem)
            Toast.makeText(context, "Nova entrada desada correctament", Toast.LENGTH_SHORT).show()
        }

        galleryButton.setOnClickListener {
            if (!isExternalPermissionGranted()) {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    EXTERNAL_PERMISSION_REQUEST_CODE
                )
            } else {
                // Sinó farem l'intent d'obrir la galeria
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                externalResult.launch(intent)
            }
        }

        uploadButton.setOnClickListener{

            val uploadTask = photoUri?.let { it1 -> productRef.child("image").putFile(it1) }

            if (uploadTask != null) {
                uploadTask.addOnSuccessListener {taskSnapshot->
                    productRef.child("image").downloadUrl.addOnSuccessListener {
                        Log.e("Firebase", "-->" + it)
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }

                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload External KO")
                }
            }

            imageView?.let { it1 ->
                Glide.with(this)
                    .load(photoUri)
                    .into(it1)
            };

        }

        return v
    }

    private val externalResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if (result.resultCode === Activity.RESULT_OK && result.data != null) {
            photoUri = result.data?.data!!
            imageView?.setImageURI(photoUri);
        }
    }

    private fun isExternalPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}