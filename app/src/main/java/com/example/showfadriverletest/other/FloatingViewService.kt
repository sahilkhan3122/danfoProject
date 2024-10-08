package com.example.showfadriverletest.other

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.showfadriverletest.R
import com.example.showfadriverletest.ui.home.MapsActivity

class FloatingViewService : Service() {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var params: WindowManager.LayoutParams? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    fun createChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        val channel =
            NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Hello! This is a notification."
        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = this
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "channelID")
                .setSmallIcon(R.drawable.cab_dummy_car)
                .setContentTitle("Showfa Driver is running")
                .setContentText("Showfa app bubble service is running")
                .setAutoCancel(true)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        createChannel(notificationManager)
        notificationManager.notify(notificationId, notificationBuilder.build())

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            Intent fullScreenIntent = new Intent(context, Drawer_Activity.class);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT );
            NotificationCompat.Builder notificationBuilder = */
        /* Do not use String.valueOf() */ /*
                    new NotificationCompat.Builder(context, getString(R.string.channel_name))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Incoming call")
                            .setContentText("(919) 555-1234")
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setCategory(NotificationCompat.CATEGORY_CALL)
                            .setChannelId(name.toString()) */
        /* Do not use String.valueOf() */ /*
                            .setFullScreenIntent(fullScreenPendingIntent, true)
                            .setAutoCancel(true);
        } else {
            startForeground(1, new Notification());
        }*/
        floatingView = LayoutInflater.from(this).inflate(R.layout.overlay_layout_bubble, null)
        val LAYOUT_FLAG: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        params!!.gravity = Gravity.TOP or Gravity.LEFT
        params!!.x = 0
        params!!.y = 100
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager!!.addView(floatingView, params)
        floatingView!!.findViewById<View>(R.id.layoutBubble).setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params!!.x
                        initialY = params!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        val Xdiff = (event.rawX - initialTouchX).toInt()
                        val Ydiff = (event.rawY - initialTouchY).toInt()
                        if (Xdiff < 10 && Ydiff < 10) {
                            val intent = Intent(applicationContext, MapsActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("fromwhere", "ser")
                            startActivity(intent)
                        }
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        params!!.x = initialX + (event.rawX - initialTouchX).toInt()
                        params!!.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager!!.updateViewLayout(floatingView, params)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingView != null) {
            windowManager!!.removeView(floatingView)
            stopSelf()
        }
    }
}
