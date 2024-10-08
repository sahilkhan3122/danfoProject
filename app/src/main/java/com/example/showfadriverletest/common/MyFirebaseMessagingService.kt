package com.example.showfadriverletest.common

import android.content.Intent
import android.util.Log
import com.example.showfadriverletest.base.BaseActivity.Companion.deviceToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "MyFirebaseMessagingService"
    private var random: Random? = null
    private var intent: Intent? = null
    var id = ""
    var messageData: kotlin.String? = ""

    var tempId = 0
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "onNewToken: $token")
        deviceToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e(
            TAG,
            "onMessageReceived() DATA:- " + message.data
        )

        random = Random()
        tempId = random!!.nextInt(9999 - 1000) + 1000

        Log.e(
            TAG,
            "onMessageReceived() remoteMessage"
        )

        val data = message.data

        val allData = message.data.toString()

        if (message.data.isNotEmpty()) {
            val body = message.data["body"].toString()
            val title = message.data["title"].toString()

            val type = message.data["type"].toString()
            val sound = message.data["sound"].toString()
            val imageUrl = message.data["image"].toString()
            Log.e(
                TAG,
                "onMessageReceived 1"
            )
            Log.e(
                TAG,
                "onMessageReceived 1"
            )
            messageData = data["data"]!!

            Log.e(
                "onMessageReceived() remoteMessage", messageData!!.toString()
            )

            Log.e(
                "onMessageReceived() remoteMessage", body
            )
            Log.e(
                "onMessageReceived() remoteMessage", type
            )
            Log.e(
                "onMessageReceived() remoteMessage",title
            )
            Log.e(
                "onMessageReceived() remoteMessage",imageUrl
            )
        }
    }
}


/*    override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    Log.e(
        "",
        "onMessageReceived() DATA:- " + remoteMessage.data
    )

    random = Random()
    tempId = random!!.nextInt(9999 - 1000) + 1000

    Log.e(
        TAG,
        "onMessageReceived() remoteMessage"
    )

    val data = remoteMessage.data

    val allData = remoteMessage.data.toString()

    if (data.isNotEmpty()) {
        val body = data["body"]
        val title = data["title"]

        val type = data["type"]
        val sound = data["sound"]
        val imageUrl = data["image"]
        Config.logger(
            TAG,
            "onMessageReceived 1"
        )
        messageData = data["data"]!!

        if (type != null &&
            type.trim { it <= ' ' }.equals("SendBulkPushNotification", ignoreCase = true)
        ) {
            createNotificationAccountVerify(body, title, tempId, type, imageUrl)
            if (MyApplication().getCurrentActivity() != null) {
                MyApplication().getCurrentActivity()?.runOnUiThread(Runnable {
                    Toast.makeText(this, "$title", Toast.LENGTH_SHORT).show()
                })
            }
        } else if (type != null && !type.equals(
                "",
                ignoreCase = true
            ) && type.equals("on_the_way", ignoreCase = true)
        ) {
            val bookingId = data["booking_id"]
            if (MyApplication().getCurrentActivity() == MapsActivity()) {
                MapsActivity().OnTheWayTrip(bookingId)
            }
        } else if (type != null && !type.equals(
                "",
                ignoreCase = true
            ) && type.equals("start_trip", ignoreCase = true)
        ) {
            val bookingId = data["booking_id"]
            if (MyApplication().getCurrentActivity() == MapsActivity()) {
                createNotificationAccountVerify(body, title, tempId, type, imageUrl)
                MapsActivity().StartTrip(bookingId)
            }
        } else if (type != null && !type.equals(
                "",
                ignoreCase = true
            ) && type.equals("complete_trip", ignoreCase = true)
        ) {
            //createNotificationAccountVerify(body,title,tempId,type);
            val bookingId = data["booking_id"]
            if (MyApplication().getCurrentActivity() == MapsActivity()) {
                MapsActivity().CompleteTrip(bookingId)
            }
        } else if (type != null && !type.equals(
                "",
                ignoreCase = true
            ) && type.equals("accepted_trip", ignoreCase = true)
        ) {
            val bookingId = data["booking_id"]
            createNotificationAccountVerify(body, title, tempId, type, imageUrl)
            if (MyApplication().getCurrentActivity() == MapsActivity()) {
                MapsActivity().AcceptedTrip(bookingId)
            }
        } else if (type != null && !type.equals(
                "",
                ignoreCase = true
            ) && type.equals("driver_arrived", ignoreCase = true)
        ) {
            val bookingId = data["booking_id"]
            if (MyApplication().getCurrentActivity() == MapsActivity()) {
                MapsActivity().ArrivedTrip(bookingId)
            }
        } else if (type != null && !type.equals(
                "",
                ignoreCase = true
            ) && type.equals("canceled_trip", ignoreCase = true)
        ) {
            val bookingId = data["booking_id"]
            if (MyApplication().getCurrentActivity() == MapsActivity()) {
                MapsActivity().CancelTrip(bookingId)
            }
        }

    } else {
        Config.logger(
            TAG,
            "onMessageReceived data null"
        )
    }
}

private fun createNotificationAccountVerify(
    body: String?,
    title: String?,
    tempId: Int,
    type: String,
    imageUrl: String?,
) {
    Config.logger(TAG, "createNotification")

    playNotificationSound()
    val notificationManager1 = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager1.cancelAll()

    var contentIntent: PendingIntent? = null

    if (type != null && type.equals("accepted_trip", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "accepted_trip", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    } else if (type != null && type.equals("ask_for_tips", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "ask_for_tips", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    } else if (type != null && type.equals("on_the_way", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "on_the_way", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    } else if (type != null && type.equals("canceled_trip", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "canceled_trip", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    } else if (type != null && type.equals("receive_money", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "receive_money", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    } else if (type != null && type.equals("transfer_money", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "transfer_money", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    } else if (type != null && type.equals("add_money", ignoreCase = true)) {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "add_money", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    } else {
        SessionSave.saveUserSession(Common.PREFRENCE_NOTIFICATION, "", this)
        intent = Intent(this, MapsActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    contentIntent = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
    )

    val notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "All Notifications"
        val channelName: CharSequence = getString(R.string.default_notification_channel_id)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = R.color.main_icon_colour
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.car) //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash_icon))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setColor(
                ContextCompat.getColor(
                    this@MyFirebaseMessagingService,
                    R.color.theme_color
                )
            )
            .setSound(notificationSoundURI) //.setStyle(new Notification.BigTextStyle().bigText(message))
            .setContentIntent(contentIntent)
            .setGroupSummary(true)
            .setGroup("KEY_NOTIFICATION_GROUP")
            .setChannelId(channelId)
        if (imageUrl != null && !imageUrl.equals(
                "",
                ignoreCase = true
            ) && !imageUrl.equals("null", ignoreCase = true)
        ) {
            val bitmap: Bitmap? = getBitmapfromUrl(imageUrl)
            notification.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bitmap)
            )
            notification.setLargeIcon(bitmap)
        }
        notificationManager.notify(tempId, notification.build())
    } else {
        val mNotificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.car /*btn_logo*/) //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash_icon/*R.mipmap.ic_launcher*/))
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColor(
                ContextCompat.getColor(
                    this@MyFirebaseMessagingService,
                    R.color.main_icon_colour
                )
            )
            .setSound(notificationSoundURI)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(contentIntent)
            .setGroupSummary(true)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setGroup("KEY_NOTIFICATION_GROUP")
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(tempId, mNotificationBuilder.build())
    }
}*/

/*    private fun playNotificationSound() {
        try {
            val alarmSound =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/echo_ringtone")
            val r = RingtoneManager.getRingtone(this, alarmSound)
            r.play()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e("awesome", "Error in getting notification image: " + e.localizedMessage)
            return null
        }
    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
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
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }*/
