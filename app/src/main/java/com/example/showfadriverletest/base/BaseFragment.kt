package com.example.showfadriverletest.base

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.pref.MyDataStore
import com.example.showfadriverletest.ui.splash.SplashActivity
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.loader.MyDialog
import com.example.showfadriverletest.view.startNewActivityWithClear
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : Fragment() {

    abstract val layoutId: Int
    abstract val bindingVariable: Int
    lateinit var dataStore: MyDataStore
    lateinit var myDialog: MyDialog

    @Inject
    lateinit var viewModel: V

    lateinit var binding: T


    @Inject
    lateinit var activity: Activity

    abstract fun setupObservable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataStore = MyDataStore(requireContext())
        myDialog = MyDialog(requireContext())
        performingDataBinding(inflater, container)
        return binding.root
    }

    private fun performingDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setVariable(bindingVariable, viewModel)
        binding.executePendingBindings()
        activity = requireActivity()
        setUpUi(binding.root)
        setupObservable()
    }
    open suspend fun checkIsSessionnOut(code: Int, msg: String): Boolean {
        if (code == 403) {
            PopupDialog.sessionExpire(
                requireContext(),
                msg
            ){
                lifecycleScope.launch {
                Constants.Login = ""
                Constants.Register = ""
                dataStore.logoutId()
                Handler(Looper.myLooper()!!).postDelayed({
                    activity.startNewActivityWithClear(SplashActivity::class.java, finish = true, null)
                }, 2000)
                }
            }
        }
        return code == 403
    }


    private fun setUpUi(view: View) {
    }

}