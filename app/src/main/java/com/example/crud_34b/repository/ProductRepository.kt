package com.example.crud_34b.repository

import android.net.Uri
import com.example.crud_34b.model.ProductModel

interface ProductRepository {
    fun uploadImage(imageUrl : Uri,callback: (Boolean, String?,String?) -> Unit)
    fun addProduct(productModel: ProductModel,callback :(Boolean,String?) -> Unit)

    fun getAllProduct(callback: (List<ProductModel>?,Boolean,String?) -> Unit)
    fun updateProduct(id:String,callback: (Boolean, String?) -> Unit)
    fun deleteData(id:String,callback: (Boolean, String?) -> Unit)
    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit)
}