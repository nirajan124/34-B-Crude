package com.example.crud_34b.ui.activity

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.R

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        notificationBindind= ActivtyNotificationBinding.inflate(layoutInflater)
        setContentView(notificatio_binding.root)

    }
fun show notification(){
   var builder = NotificationCompat.Builder(this@NotificationActivity,CHANNEL_ID)
        if(Build.Version.SDK_INT>=Build.VERSION_CODES.0){
            Var channel:NotificationChannel=NotificationChannel(CHANNEL_ID,name:"My channel",
                NotificationManager.IMPORTANCE_DEFAULT
                )
            Var manager:NotificationManager=
                    getSystemService(NOTIFICATION_SERVICE) as notificationManager
            manager.createNotificationChannel(channel)
            builder.setSmallIcon(R.drawable.baseline_add_a_photo_24)
                .setContextTitle("offer")
                .setContextText("Grab your Discount")

        }
        esle
}