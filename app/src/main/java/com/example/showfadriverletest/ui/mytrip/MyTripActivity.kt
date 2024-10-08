package com.example.showfadriverletest.ui.mytrip

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.databinding.ActivityMyTripBinding
import com.example.showfadriverletest.fragment.PastFragment
import com.example.showfadriverletest.fragment.UpcomingFragment
import com.example.showfadriverletest.ui.mytrip.viewmodel.MyTripViewModel
import com.example.showfadriverletest.ui.notification.NotificationActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyTripActivity : BaseActivity<ActivityMyTripBinding, MyTripViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_my_trip
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activityHeader.icNotification.visibility = View.VISIBLE
            activityHeader.icNotification.setOnClickListener {
                startActivity(Intent(this@MyTripActivity, NotificationActivity::class.java))
            }
            tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"))
            tabLayout.addTab(binding.tabLayout.newTab().setText("Past"))
            activityHeader.tvTitle.text = getString(R.string.my_trips)
            activityHeader.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }
    }

    override fun setUpObserver() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { fragmentSet(UpcomingFragment()) }
                    1 -> { fragmentSet(PastFragment()) }
                    else -> { fragmentSet(UpcomingFragment()) }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun fragmentSet(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}




