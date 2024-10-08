package com.example.showfadriverletest.other

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.showfadriverletest.R

class BubbleService : Service() {
    var TAG = "BubbleService"
    private var vto:ViewTreeObserver? = null
    private var mWindowManager: WindowManager? = null
    private var mOverlayView: View? = null
    var mWidth = 0
    var layout: RelativeLayout? = null
    private var counterFab: ImageView? = null
    var activity_background = false
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

   /* override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false)
        }
        if (mOverlayView == null) {
            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout_bubble, null)
            val LAYOUT_FLAG: Int
            LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

//            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_PHONE,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )


            //Specify the view position
            params.gravity =
                Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
            params.x = 0
            params.y = 100
            *//* params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;*//*mWindowManager = getSystemService(
                WINDOW_SERVICE
            ) as WindowManager
            mWindowManager!!.addView(mOverlayView, params)
            val display = mWindowManager!!.defaultDisplay
            val size = Point()
            display.getSize(size)
            counterFab = mOverlayView!!.findViewById<ImageView>(R.id.fabHead)
            layout = mOverlayView!!.findViewById<RelativeLayout>(R.id.layoutBubble)
             vto = layout.getViewTreeObserver()
            val viewTreeObserver: ViewTreeObserver = layout.getViewTreeObserver()
            vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    val width = layout.getMeasuredWidth()

                    //To get the accurate middle of the screen we subtract the width of the floating widget.
                    mWidth = size.x - width
                }
            })

            layout.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
            object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            Log.e("MotionEvent", "ACTION_DOWN")
                            //remember the initial position.
                            initialX = params.x
                            initialY = params.y


                            //get the touch location
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
                            Log.e("MotionEvent", "ACTION_UP")
                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
                            *//*if(activity_background){

                            }*//*
                            val xDiff = event.rawX - initialTouchX
                            val yDiff = event.rawY - initialTouchY
                            if (Math.abs(xDiff) < 5 && Math.abs(yDiff) < 5) {
                              *//*  Drawer_Activity.isAppTerminate = true*//*
                                stopSelf()
                                val intent = Intent(baseContext, MapsActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                //                                Drawer_Activity.activity.callCloseFromBubble();
                                //close the service and remove the fab view
                            }


                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            val middle = mWidth / 2
                            val nearestXWall = (if (params.x >= middle) mWidth else 0).toFloat()
                            params.x = nearestXWall.toInt()
                            mWindowManager!!.updateViewLayout(mOverlayView, params)
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val xDiff2 = Math.round(event.rawX - initialTouchX)
                            val yDiff2 = Math.round(event.rawY - initialTouchY)


                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff2
                            params.y = initialY + yDiff2

                            //Update the layout with new X & Y coordinates
                            mWindowManager!!.updateViewLayout(mOverlayView, params)
                            return true
                        }
                    }
                    return false
                }
            })
        }
        return super.onStartCommand(intent, flags, startId)
    }*/

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mOverlayView != null) mWindowManager!!.removeView(mOverlayView)
    }
}