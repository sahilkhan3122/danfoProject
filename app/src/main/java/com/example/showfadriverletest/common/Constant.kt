package com.example.showfadriverletest.common

import android.location.Location

object Constants {
    const val TYPE_TIP_BOOK_LATER_NOTIFICATION = "AskForTipsToPassengerForBookLater"
    const val TYPE_TIP_BOOK_NOW_NOTIFICATION = "AskForTipsToPassenger"
    const val LAUNCH_SECOND_ACTIVITY = 12
    const val TRIP_DETAILS = "TRIP_DETAILS"
    const val ADD_MONEY_REQUEST_CODE = 123
    const val ADD_PROMOCODE_RESULT = 108
    const val SELECT_AMBULANCE_RESULT = 167
    var newgpsLatitude = "0" //"50.605846";
    var WEBVIEW_URL = "webviewUrl" //"50.605846";
    var LANGUAGE_SWAHILI = "SW" //"50.605846";
    var LANGUAGE_ENGLISH = "EN" //"50.605846";
    var GENDER_MALE = "0" //"50.605846";
    var GENDER_FEMALE = "0" //"50.605846";
    var newgpsLongitude = "0" //"7.206919";
    var HEADER_HEIGHT = 0
    var HEADER_WIDTH = 0
    var STATUS_BAR_HEIGHT = 0
    var DEVICE_HEIGHT = 0
    var pattern  = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    var EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
    var USERID = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var mDeclination: Float? = null
    var location: Location? = null
    const val NOTIFICATION_KEY_LOGOUT = "logout"
    var INVALID_ID = -Int.MAX_VALUE
    var PARAM_SOCIAL_TYPE_GOOGLE = "Google"
    var PARAM_SOCIAL_TYPE_FACEBOOK = "Facebook "
    var PARAM_PROMO_CODE = "promocode"
    var TEMP_CHANGE = "TEMPCHANGE"
    var DEVICE_WIDTH = 0
    var SOS_NUMBER = ""
    var PASSANGER_NUMBER = ""
    var OTP_AUTO_FILL = false
    var IS_LOGIN_SCREEN = false
    var IS_FROM_PAYMENT = false
    var IS_FROM_PAYMENT_SUCCESS = false
    var IS_METER_ON = false
    var IS_CALL_ESTIMATE_FARE = false
    var TermsConditionUrl = ""
    var PrivacyPolicy = ""
    var CancelReason = ""
    var IS_NOTIFICATION_ON_THE_WAY_TRIP = false
    var IS_SOCKET_ON_THE_WAY_TRIP = false
    var IS_NOTIFICATION_ACCEPTED_TRIP = false
    var IS_SOCKET_ACCEPTED_TRIP = false
    var IS_NOTIFICATION_ARRIVED_TRIP = false
    var IS_SOCKET_ARRIVED_TRIP = false
    var IS_NOTIFICATION_START_TRIP = false
    var IS_SOCKET_START_TRIP = false
    var IS_NOTIFICATION_COMPLETE_TRIP = false
    var IS_SOCKET_COMPLETE_TRIP = false
    var IS_NOTIFICATION_CANCELLED_TRIP = false
    var IS_SOCKET_CANCELLED_TRIP = false
    var SAVING_WALLET_MIN_PERCENTAGE = "0"
    const val INTENT_BOOKING_TYPE = "bookingType"
    const val INTENT_BOOKING_TYPE_BOOK_NOW = "book_now"
    const val INTENT_BOOKING_TYPE_BOOK_LATER = "book_later"
    const val BOOKING_TYPE_BOOKLATER = "book_later"
    const val ADVANCE_BOOKING = "AdvanceBooking"
    const val BOOKING = "Booking"
    const val INTENT_BOOKING_ID = "bookingId"
    const val INTENT_NOFI_CHAT_DATA = "nofi_chat_data"
    const val INTENT_RECEIVER_ID = "receiverId"
    const val INTENT_USER_NAME = "userName"
    const val INTENT_VEHICLE_TYPE_ID = "vehicleTypeId"
    const val INTENT_CUSTOMER_NAME = "customerName"
    const val INTENT_MESSAGE = "userMessage"
    const val INTENT_TYPE = "type"
    const val PREFERENCE_USER = "prefUser"
    const val PASS_CARD_SELECT = "passCardSelect"
    const val PASS_CARD_SELECT_DATA = "passCardSelectData"
    const val PASS_CARD_TYPE = "passCardType"
    const val PASS_CARD_NUM = "passCardNum"
    const val PASS_CAR_SELECTED_POSITION = "passCarSelectedPos"
    const val PASS_LOGIN = "passLogin"
    const val PASS_FLAT_RATE = "flatRate"
    const val FROM = "from"
    const val booking_id = "booking_id"
    const val PASS_ID_CARD_SELECT = 101
    const val SELECT_CARD = 102
    const val PASS_ID_CARD_SELECT_DATA = 103
    const val PASS_ID_FLATE_RATE = 104
    const val PASS_PAYMENT_CODE = 105
    const val PASS_PAYMENT_RESPONCE = "paymentResponce"
    const val PASS_RESPONCE = "responce"
    const val PAYMENT_PESA_PAL = "pesapal"
    const val PAYMENT_PESA_PAL_URL = "payment_url"
    const val PAYMENT_METHOD_M_PESA = "m_pesa"
    const val PAYMENT_METHOD_WALLET = "wallet"
    const val PAYMENT_METHOD_CASH = "cash"
    const val PAYMENT_METHOD_CARD = "card"
    const val PAYMENT_METHOD_JAMBOPAY = "jambopay"
    const val SELECTED_TYPE = "select_option_type"
    const val VEHICLE_TYPE_AMBULANCE = "ambulance"
    const val REPORT_TYPE_WEEKLY = "weekly"
    const val REPORT_TYPE_MONTHLY = "monthly"
    const val REPORT_TYPE_DAILY = "daily"
    var CURRENCY = "KES"
    var isFromAdapter = "isFromAdapter"
    var isFromAddMoney = "isFromAddMoney"
    var paymentType = "paymentType"
    var cardId = "cardId"
    var cardPin = "cardPin"
    var promocode = "promocode"
    var discount = "discount"
    var card = "card"
    var position = "position"
    var promo = "promo"
    var webcode = "webcode"
    var addressType = "addressType"
    var selectedFavAddress = "selectedFavAddress"
    var office = "office"
    var other = "other"
    var home = "home"
    var INTENT_ADDRESS_TITLE = "address_title"
    var INTENT_ADDRESS_DESCRIPTION = "address_description"
    var INTENTT_ADDRESS_LATITUDE = "address_latitude"
    var INTENTT_ADDRESS_LONGITUDE = "address_longitude"
   /* var equipmentSelectedLists: ArrayList<AmbulancePackageListResponse> =
        ArrayList<AmbulancePackageListResponse>()*/
    var ambulanceId = ""
    var equipmentSelectedIdStr = ""
    var ambulanceEstimatedFare = ""
    var ambulancePackageId = ""
    var equipmentAdditionalNote = ""

    var ImageBaseUrl = ""
    var IS_LIVE_URL = false
    var USER_VEHICLE_TYPE = "VehicleType"
    var VEHICLE_TYPE_BODA = "boda"
    var CURRENCY_TYPE = "KES"
    var Login = ""
    var Register = ""
    var flagCollect = false
    var flagPermission = false
    var flagResult = false
    //LOG Constants
    const val LOG_ERROR_TAG = "Showfa_error"
    const val LOG_TAG = "Showfa_Tag"

    var selectVehicleModel = true
    var selectVehicleYear = true
    var selectVehicleManufactuer = true
    var selectLeftImage = false
    var selectRightImage = false
    var selectFrontImage = false
    var selectBackImage = false
    var selectInsideImage = false

    var EditVehicleDetail = false
    var UploadDoc = false
}
