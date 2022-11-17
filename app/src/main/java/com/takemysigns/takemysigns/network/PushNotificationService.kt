package com.takemysigns.takemysigns.network

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.takemysigns.takemysigns.base.TakeMySignsApp


class PushNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("PushNotificationService -- token", token)
        TakeMySignsApp.setPushToken(token)
    }
}