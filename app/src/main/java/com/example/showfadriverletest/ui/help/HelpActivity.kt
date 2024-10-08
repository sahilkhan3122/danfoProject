package com.example.showfadriverletest.ui.help

import android.os.Bundle
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.databinding.ActivityHelpBinding
import com.example.showfadriverletest.ui.help.viewmodel.HelpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpActivity : BaseActivity<ActivityHelpBinding, HelpViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_help
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            header.tvTitle.text = getString(R.string.supportt)
            header.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }
    }

    override fun setUpObserver() {
        mViewModel.setNavigator(this)
    }
}