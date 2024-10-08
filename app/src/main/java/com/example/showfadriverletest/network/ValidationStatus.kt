package com.example.showfadriverletest.network

import android.app.Activity

enum class ValidationStatus {
    EMPTY_MOBILE_NO,
    INVALID_MOBILE_NUMBER

}

object Validation {
    fun showMessageDialog(activity: Activity, validationStatus: ValidationStatus) {
        val message = getMessage(activity, validationStatus)
        if (message.isNotEmpty()) {
           /* activity.showErrorAlert(message = message)*/
        }
    }

    fun getMessage(activity: Activity, it: ValidationStatus): String {
        return when (it) {
            ValidationStatus.EMPTY_MOBILE_NO ->"error"
            ValidationStatus.INVALID_MOBILE_NUMBER -> "error"
        }
    }
}