package com.example.crud_34b.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud_34b.model.ProductModel
import com.example.crud_34b.repository.ProductRepository

class ProductViewModel(val repository: ProductRepository) : ViewModel() {
    fun uploadImage(imageUrl: Uri, callback: (Boolean, String?, String?) -> Unit) {
        repository.uploadImage(imageUrl) { success, imageUrl, imageName ->
            callback(success, imageUrl, imageName)
        }
    }

    fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit) {
        repository.addProduct(productModel, callback)
    }

    private var _productList = MutableLiveData<List<ProductModel>?>()

    var productList = MutableLiveData<List<ProductModel>?>()
        get() = _productList

    var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState



    fun fetchProduct(){
        _loadingState.value = true
        repository.getAllProduct { productList, success, message ->
            if(productList!=null){
                _loadingState.value = false
                _productList.value = productList
            }
        }
    }
}