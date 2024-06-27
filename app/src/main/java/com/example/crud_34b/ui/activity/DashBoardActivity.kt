package com.example.crud_34b.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_34b.R
import com.example.crud_34b.adapter.ProductAdapter
import com.example.crud_34b.databinding.ActivityDashBoardBinding
import com.example.crud_34b.model.ProductModel
import com.example.crud_34b.repository.ProductRepositoryImpl
import com.example.crud_34b.viewmodel.ProductViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DashBoardActivity : AppCompatActivity() {
    lateinit var dashBoardBinding: ActivityDashBoardBinding

    lateinit var productAdapter: ProductAdapter

    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dashBoardBinding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(dashBoardBinding.root)

        val repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)
        productViewModel.fetchProduct()

        productAdapter = ProductAdapter(this@DashBoardActivity,
            ArrayList())

//        dashBoardBinding.recyclerView.layoutManager = LinearLayoutManager(this)
//        dashBoardBinding.recyclerView.adapter = productAdapter

        dashBoardBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashBoardActivity)
            adapter = productAdapter
        }

        productViewModel.loadingState.observe(this){loading->
            if(loading){
                dashBoardBinding.progressBar2.visibility = View.VISIBLE
            }else{
                dashBoardBinding.progressBar2.visibility = View.GONE
            }
        }

        productViewModel.productList.observe(this){products->
            products?.let {
                productAdapter.updateData(it)
            }
        }

//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//               var id = productAdapter.getProductID(viewHolder.adapterPosition)
//               var imageName = productAdapter.getImageName(viewHolder.adapterPosition)
//
//                ref.child(id).removeValue().addOnCompleteListener {
//                    if(it.isSuccessful){
//                        storageRef.child("products").child(imageName).delete()
//                        Toast.makeText(applicationContext,"Data deleted",
//                            Toast.LENGTH_LONG).show()
//
//
//                    }else{
//                        Toast.makeText(applicationContext,it.exception?.message,
//                            Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }).attachToRecyclerView(dashBoardBinding.recyclerView)


        dashBoardBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@DashBoardActivity,
                AddProductActivity::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}