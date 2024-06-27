package com.example.crud_34b.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.crud_34b.R

class LoadingUtils (val activity:Activity){
    private lateinit var alertDialog: AlertDialog
    fun showLoading(){
        val dialogView=activity.layoutInflater.inflate(R.layout.loading_dialog,null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()

    }
    fun dismiss(){
        alertDialog.dismiss()

    }
}