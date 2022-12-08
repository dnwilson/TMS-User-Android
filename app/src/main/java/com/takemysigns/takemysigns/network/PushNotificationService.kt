package com.takemysigns.takemysigns.network

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.main.MainActivity


class PushNotificationService : FirebaseMessagingService() {
    private val TAG = PushNotificationService::class.java.name

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("PushNotificationService -- token", token)
        TakeMySignsApp.setPushToken(token)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Push received with remoteMessage: $remoteMessage")
//        val json = (remoteMessage.data as Map<String, String>?)?.let { JSONObject(it) }
//        val uri = json?.getString("uri")
        Log.i(TAG, "Push received with uri: ${remoteMessage.data}")

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val uri = remoteMessage.data["uri"]

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(TakeMySignsApp.REQUESTED_PAGE, uri)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TakeMySignsApp.NOTIFICATION_ID,
                "TakeMySigns-User",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, TakeMySignsApp.NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_tms_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())

//        val notification = remoteMessage.notification
//
//        // Check if message contains a data payload.
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.data)
//            val json = (remoteMessage.data as Map<String, String>?)?.let { JSONObject(it) }
//            Log.i(TAG, "Push received: $json")
//            try {
//                val uri = json.getString("uri")
//                val resultIntent = Intent(baseContext, MainActivity::class.java)
//                resultIntent.putExtra(SwaggApp.REQUESTED_PAGE, uri)
//                showNotificationMessage(
//                    baseContext,
//                    notification!!.title,
//                    notification!!.body,
//                    resultIntent
//                )
//                parsePushJson(baseContext, json)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
        }
    }

//    private fun parsePushJson(context: Context, json: JSONObject) {
//        try {
////            boolean isBackground = json.getBoolean("isBackground");
//            val data = json.getJSONObject("data")
//            val title = data.getString("title")
//            val message = data.getString("alert")
//            val url = data.getString("uri")
//            val resultIntent = Intent(context, MainActivity::class.java)
//            resultIntent.putExtra(TakeMySignsApp.REQUESTED_PAGE, url)
//            showNotificationMessage(context, title, message, resultIntent)
//        } catch (e: JSONException) {
//            Log.e(TAG, "Push message json exception: " + e.message)
//        }
//    }

//    private fun showNotificationMessage(
//        context: Context,
//        title: String,
//        message: String,
//        intent: Intent
//    ) {
//        notificationUtils = NotificationUtils(context)
//        intent.putExtras(intent.extras!!)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        notificationUtils.showNotificationMessage(title, message, intent)
//    }

//}

class NotificationUtils {
    private val TAG: String = NotificationUtils::class.java.simpleName
    private var context: Context? = null

    constructor() {}
    constructor(mContext: Context?) {
        this.context = mContext
    }

    fun showNotificationMessage(title: String?, message: String?, intent: Intent) {

        // Check for empty push message
        if (TextUtils.isEmpty(message)) return
        if (isAppIsInBackground(context)) {
            val notificationId: Int = 82416
            val icon: Int = R.drawable.logo
            val resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val inboxStyle = NotificationCompat.InboxStyle()
            val mBuilder = NotificationCompat.Builder(context!!, TakeMySignsApp.NOTIFICATION_ID)
            val notification: Notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(context!!.resources, icon))
                .setContentText(message)
                .build()
            val notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, notification)
        } else {
            intent.putExtra("title", title)
            intent.putExtra("message", message)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            context!!.startActivity(intent)
        }
    }

    companion object {
        /**
         * Method checks if the app is in background or not
         *
         * @param context
         * @return
         */
        fun isAppIsInBackground(context: Context?): Boolean {
            var isInBackground = true
            val am = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
            return isInBackground
        }
    }
}
