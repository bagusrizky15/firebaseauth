package com.rivvana.firebaseauth

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
class MessageService : FirebaseMessagingService() {

    fun generateNotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }


}
