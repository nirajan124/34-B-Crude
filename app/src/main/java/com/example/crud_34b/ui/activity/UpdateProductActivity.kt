package com.example.crud_34b.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.R
import com.example.crud_34b.databinding.ActivityUpdateProductBinding
import com.example.crud_34b.model.ProductModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class UpdateProductActivity : AppCompatActivity() {
    lateinit var updateProductBinding: ActivityUpdateProductBinding
    var firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")
    var id = ""
    var imageName = ""
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null
    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = firebaseStorage.reference

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)
        registerActivityForResult()

        var product: ProductModel? = intent.getParcelableExtra("product")
        id = product?.id.toString()
        imageName = product?.imageName.toString()
        updateProductBinding.editTextProductNameUpdate.setText(product?.name)
        updateProductBinding.editTextProductPriceUpdate.setText(product?.price.toString())
        updateProductBinding.editTextProductDescUpdate.setText(product?.description)

        Picasso.get().load(product?.url).into(updateProductBinding.imageUpdate)
        updateProductBinding.btnUpdate.setOnClickListener {
            uploadImage()
        }

        updateProductBinding.imageUpdate.setOnClickListener{
            var permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                Manifest.permission.READ_MEDIA_IMAGES
            }else{
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            if (ContextCompat.checkSelfPermission(this,permissions) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(permissions),1)
            }else{
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                activityResultLauncher.launch(intent)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun registerActivityForResult(){
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->

                val resultcode = result.resultCode
                val imageData = result.data
                if(resultcode == RESULT_OK && imageData != null){
                    imageUri = imageData.data
                    imageUri?.let {
                        Picasso.get().load(it).into(updateProductBinding.imageUpdate)
                    }
                }

            })
    }

    fun uploadImage(){

        var imageReference = storageRef.child("products").child(imageName)

        imageUri?.let { url->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {downloadUrl->
                    var imagesUrl = downloadUrl.toString()
                    updateProduct(imagesUrl)
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage,
                    Toast.LENGTH_LONG).show()
            }
        }


    }
    fun updateProduct(url: String){
        var updatedName : String = updateProductBinding.editTextProductNameUpdate.text.toString()
        var updatedPrice : Int = updateProductBinding.editTextProductPriceUpdate.text.toString().toInt()
        var updatedDesc : String = updateProductBinding.editTextProductDescUpdate.text.toString()

        var data = mutableMapOf<String,Any>()
        data["name"] = updatedName
        data["price"] = updatedPrice
        data["description"] = updatedDesc
        data["url"] = url

        ref.child(id).updateChildren(data).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(applicationContext,"Data updated",
                    Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(applicationContext,it.exception?.message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}