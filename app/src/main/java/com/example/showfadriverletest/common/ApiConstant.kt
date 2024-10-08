package com.example.showfadriverletest.common

import com.example.showfadriverletest.BuildConfig
import com.example.showfadriverletest.response.init.InitResponse

object ApiConstant {

    ////////////////////////////////// url constant /////////////////////////////////////////////////
    const val EndPoint_Url: String = "api/v3/driver_api/"
    const val BASE_URL: String = "https://dev.showfaride.com/".plus(EndPoint_Url)
    const val Image_URL: String = "https://dev.showfaride.com/"
    const val API_KEY: String = "key"
    var API_VALUE: String = "DPS\$951"
    const val USER_PREF_INIT_DATA_KEY = "prefInItData"

    ////////////////////////////////// init constant /////////////////////////////////////////////////
    const val END_POINT_INIT = "init/"
    const val DEVICE = "android_driver/"
    const val init = "${END_POINT_INIT}${DEVICE}${BuildConfig.VERSION_NAME}"

    ////////////////////////////////// login constant /////////////////////////////////////////////////
    const val END_POINT_LOGIN = "login"
    const val USER_NAME = "username"
    const val LAT = "lat"
    const val LNG = "lng"
    const val DEVICE_TYPE = "device_type"
    const val DEVICE_TOKEN = "device_token"

    //////////////////////////////////  Resend Otp /////////////////////////////////////////////////
    const val END_POINT_RESEND_OTP = "resend_otp"
    const val MOBILE_NO = "mobile_no"

    //////////////////////////////////  Register Otp/////////////////////////////////////////////////
    const val END_POINT_REGISTER_OTP = "register_otp"
    const val EMAIL = "email"
    const val REGISTER_MOBILE_NO = "mobile_no"

    ////////////////////////////////// Privacy policy And Term Of Service Url/////////////////////////////////////////////////
    const val TERM_OF_SERVICE_URL = "https://www.showfaride.com/terms-condition"
    const val PRIVACY_POLICY_URL = "https://www.showfaride.com/privacy-policy"
    var ISCHECKED_TERM_OF_SERVICE = false
    var ISCHECKED_PRIVACY_POLICY = false

    /*vehicle_type_manufacturer_list*/
    const val END_POINT_VEHICLE_LIST = "vehicle_type_manufacturer_list"

    //////////////////////////////////Upload Docs /////////////////////////////////////////////////
    const val ENDPOINT_UPLOAD_DOC = "upload_docs"
    const val WEB_PARAM_DOC_IMAGE = "image"
    const val WEB_PARAM_DOC_IMAGE_REGISTER = "profile_image"

    ////////////////////////////////// Register constant /////////////////////////////////////////////////
    const val END_POINT_REGISTER = "register"
    const val DRIVER_ROLE = "driver_role"
    const val CAR_TYPE = "car_type"
    const val OWNER_NAME = "owner_name"
    const val OWNER_EMAIL = "owner_email"
    const val OWNER_MOBILE_NUMBER = "owner_mobile_no"
    const val FIRST_NAME = "first_name"
    const val LAST_NAME = "last_name"
    const val DOB = "dob"
    const val GENDER = "gender"
    const val ACCOUNT_HOLDER_NAME = "account_holder_name"
    const val BANK_NAME = "bank_name"
    const val BANK_BRANCH = "bank_branch"
    const val ACCOUNT_NUMBER = "account_number"
    const val DEVICY_TYPE = "device_type"
    const val ADDRESS = "address"
    const val PLATE_NUMBER = "plate_number"
    const val YEAR_OF_MANUFACTURE = "year_of_manufacture"
    const val VEHICLE_TYPE_MODEL_NAME = "vehicle_type_model_name"
    const val VEHICLE_TYPE_MODEL_ID = "vehicle_type_model_id"
    const val VEHICLE_TYPE_MANUFACTURER_NAME = "vehicle_type_manufacturer_name"
    const val VEHICLE_TYPE_MANUFACTURER_ID = "vehicle_type_manufacturer_id"
    const val NO_OF_PASSANGER = "no_of_passenger"
    const val VEHICLE_TYPE = "vehicle_type"
    const val INVITE_CODE = "invite_code"
    const val WORK_WITH_OTHER_COMPANY = "work_with_other_company"
    const val OTHER_COMPANY_NAME = "other_company_name"
    const val COMPANY_ID = "company_id"
    const val CAR_LEFT = "car_left"
    const val CAR_RIGHT = "car_right"
    const val CAR_FRONT = "car_front"
    const val CAR_BACK = "car_back"
    const val CAR_INSIDE = "car_interior"
    const val NTSA_INSPECTION_IMAGE = "ntsa_inspection_image"
    const val NTSA_EXP_DATE = "ntsa_exp_date"
    const val COMREHENSIVE_IMAGE = "psv_comprehensive_insurance"
    const val COMREHENSIVE_EXP = "psv_comprehensive_insurance_exp_date"

    const val VEHICLE_LOG_BOOK = "vehicle_log_book_image"
    const val DRIVER_LICENCE_IMAGE = "driver_licence_image"
    const val DRIVER_LICENCE_EXP_DATE = "driver_licence_exp_date"
    const val NTSA_BUDGE = "psv_badge_image"
    const val BUDGE_EXP = "psv_badge_exp_date"
    const val NATIONAL_ID_IMAGE = "national_id_image"
    const val POLICE_CLEARNCE_CERTI = "police_clearance_certi"
    const val NATIONAL_ID_NUMBER = "national_id_number"
    const val DRIVER_PSV_LISENCE = "driver_psv_license"
    const val PROFILE_IMAGE = "profile_image"
    const val CAR_INTRIOR = "car_Interior"
    const val VEHICLE_COLOR = "vehicle_color"
    const val VEHICLE_INSURANCE = "vehicle_insurance_exp_date"
    const val POLICE_CLEARNCE_EXP = "police_clearance_exp_date"

    ////////////////////////////////// past trip /////////////////////////////////////////////////
    const val END_POINT_PAST = "past_booking_history/"
    const val VERSION_CODE_PAST = "118/"
    const val DEVICE_ID = "1"
    const val END_POINT_PAST_TRIP = "${END_POINT_PAST}"

    //////////////////////////////////  feature  trip /////////////////////////////////////////////////
    const val END_POINT_FEATURE = "future_booking_history/"
    const val END_POINT_FEATURE_TRIP = "${END_POINT_FEATURE}"


    ////////////////////////////////// trip Details /////////////////////////////////////////////////
    const val END_POINT_TRIP_DETAIL = "trip_details/"
    const val USER_ID = "641"
    const val END_POINT_TRIP_DETAILS = "${END_POINT_TRIP_DETAIL}${USER_ID}"

    //////////////////////////////////  Earning Report /////////////////////////////////////////////////
    const val END_POINT_EARNING = "driver_earnings"
    const val TYPE = "type"
    const val DRIVERID = "driver_id"
    const val FROMDATE = "from_date"
    const val TODATE = "to_date "

    ////////////////////////////////// support /////////////////////////////////////////////////
    const val END_POINT_SUPPORT = "generate_ticket"


    ////////////////////////////////// subscription /////////////////////////////////////////////////
    const val END_POINT_SUBSCRIPTION = "subscription_list"


    ////////////////////////////////// subscriptio Purchase /////////////////////////////////////////////////
    const val END_POINT_SUBSCRIPTION_PURCHASE = "subscription_purchase"
    const val SUBSCRIPTION_ID = "subscription_id"

    ////////////////////////////////// notification_list/////////////////////////////////////////////////
    var END_POINT_NOTIFICATION = "notification_list/"
    var NOTIFICATION_END_POINT = END_POINT_NOTIFICATION


    //////////////////////////////////  clear notification/////////////////////////////////////////////////

    var END_POINT_NOTIFICATIONN = "notification_clear/"
    var NOTIFICATION_CLEAR_END_POINT = END_POINT_NOTIFICATIONN


    ////////////////////////////////// Wallet history /////////////////////////////////////////////////
    const val END_POINT_Wallet_history = "wallet_history"
    const val DRIVER_ID = "driver_id"
    const val PAGE = "page"
    const val FROM_DATE = "from_date"
    const val TO_DATE = "to_date"
    const val PAYMENT_TYPE = "payment_type"
    const val TRANSACTION_TYPE = "transaction_type"

    ////////////////////////////////// addMoney history /////////////////////////////////////////////////

    const val END_POINT_ADD_MONEY = "add_money"
    const val AMOUNT = "amount"

    ////////////////////////////////// saving Wallet history /////////////////////////////////////////////////
    const val END_POINT_SAVING_WALLET = "saving_wallet_history"

    ////////////////////////////////// saving Wallet history /////////////////////////////////////////////////

    const val END_POINT_EDIT_PROFILE = "update_basic_info"
    const val CARTYPE = "car_type"
    const val FIRSTNAME = "first_name"
    const val LASTNAME = "last_name"
    const val GENDERR = "gender"
    const val PAYMENTMETHOD = "payment_method"
    const val MOBILENO = "mobile_no"
    const val OWNERNAME = "owner_name"
    const val OWNEREMAIL = "owner_email"
    const val OWNERNO = "owner_mobile_no"
    const val WEB_PARAM_PROFILE_IMAGE = "profile_image"

    ////////////////////////////////// update vehicleInfo /////////////////////////////////////////////////
    const val END_POINT_EDIT_VEHICLE_INFO = "update_vehicle_info"

    /////////////////////////////////// update vehicleDocument/////////////////////////////////////////////////
    const val END_POINT_EDIT_VEHICLE_DOCUMENT = "update_docs"


    ////////////////////////////////// logout /////////////////////////////////////////////////
    var token = ""
    var userid = ""
    var END_POINT_LOGOUT = "logout/"
    var LOGOUT_END_POINT = END_POINT_LOGOUT
    var CHAT_HISTORY = "chat_history/"

    //////////////////////////////////  delete account/////////////////////////////////////////////////
    const val END_POINT_DELETE_ACCOUNT = "delete_account"
    const val DELETE_DRIVER_ID = "driver_id"


    ////////////////////////////////// change duty /////////////////////////////////////////////////
    const val CHANGE_DUTY = "change_duty"


    ////////////////////////////////// complete Trip  /////////////////////////////////////////////////
    const val CANCEL_TRIP = "cancel_trip"
    const val BOOKING_ID = "booking_id"
    const val CANCEL_RESON = "cancel_reason"
    const val DISTANCE = "distance"
    const val DROP_OF_LNG = "dropoff_lng"
    const val DROP_OF_LAT = "dropoff_lat"
    const val COMPLETE_TRIP = "complete_trip"
    const val RATING = "review_rating"
    const val RATING_CUST = "rating"
    const val COMMENT = "comment"

    //////////////////////////////chat api////////////////////
    const val CHAT_TO_CUSTOMER = "chat"
    const val SENDER_TYPE = "sender_type"
    const val SENDER_ID = "sender_id"
    const val RECIEVER_ID = "receiver_id"
    const val RECIEVER_TYPE = "receiver_type"
    const val MESSAGE = "message"
    const val CUSTOMER = "customer"
    const val DRIVER = "driver"


    ////////////////////////////////// Socket On/Off key /////////////////////////////////////////////////
    var SocketUrl = "https://dev.showfaride.com:8080"
    const val SOCKET_ON_CANCEL_BOOKING_BEFORE_ACCEPT = "cancel_booking_before_accept"
    const val SOCKET_ON_BOOKING_REQUEST_BOOK_NOW = "forward_booking_request"
    const val SOCKET_ON_ACCEPT_BOOKING_BOOK_NOW = "accept_booking_request"
    const val SOCKET_ON_DRIVER_ARRIVED = "driver_arrived"
    const val SOCKET_ON_ARRIVED_AT_PICKUP_LOCATION = "arrived_at_pickup_location"
    const val SOCKET_ON_BOOKING_INFO_BOOK_NOW_START_TRIP = "start_trip"
    const val SOCKET_ON_ON_THE_WAY_BOOKING_REQUEST = "on_the_way_booking_request"
    const val SOCKET_ON_VERIFY_CUSTOMER = "verify_customer"
    const val SOCKET_ON_RECEIVE_TIPS = "receive_tips"
    const val SOCKET_ON_CANCEL_TRIP = "cancel_trip"
    const val SOCKET_ON_PAYMENT_FAILD_MPESA = "payment_failed_mpesa"
    const val SOCKET_ON_PAYMENT_SUCCESS_MPESA = "payment_success_mpesa"
    const val SOCKET_ON_UPDATE_DRIVER_LAT_LONG = "update_driver_location"
    const val SOCKET_ON_ERROR = "error"
    const val BOOKING_TYPE_BOOKNOW = "book_now"
    const val SOCKET_EMIT_UPDATE_DRIVER_LAT_LONG = "update_driver_location"

    //String SOCKET_EMIT_UPDATE_DRIVER_LAT_LONG_LIVE_TRACING = "live_tracking";
    const val SOCKET_ON_REQUEST_CODE_FOR_COMPLETE_TRIP = "request_code_for_complete_trip"
    const val SOCKET_ON_BOOKING_INFO_BOOK_LATER_START_TRIP = "start_trip"
    const val SOCKET_ON_BOOKING_REQUEST_BOOK_LATER = "AriveAdvancedBookingRequest"
    const val SOCKET_ON_BOOKING_INFO_BOOK_LATER = "AdvancedBookingInfo"
    const val SOCKET_ON_CANCEL_TRIP_BY_PASS_BOOK_NOW = "DriverCancelTripNotification"
    const val SOCKET_ON_CANCEL_TRIP_BY_PASS_BOOK_LATER =
        "AdvancedBookingDriverCancelTripNotification"
    const val SOCKET_ON_ARRIVED_BOOKLATER = "AdvanceBookingDriverArrivedAtPickupLocation"
    const val SOCKET_ON_BOOK_LATER_DRIVER_NOTIFY = "BookLaterDriverNotify"
    const val SOCKET_ON_SESSION_ERROR = "SessionError"
    const val SOCKET_ON_START_TRIP_ERROR = "StartTripTimeError"
    const val SOCKET_ON_RECEIVE_MESSAGE = "receive_message"
    const val SOCKET_ON_ADD_EXTRA_DROP_OFF = "AddExtraDropoffLocationNotification"
    const val SOCKET_ON_UPDATE_DROPOFF = "UpdateDropoffLocation"
    const val SOCKET_ON_BROADCAST = "broadcast"

    ////////////////////////////////// Socket Emit Key /////////////////////////////////////////////////
    const val SOCKET_CALL_REJECT_BOOKING_REQUEST_BOOK_NOW =
        "forward_booking_request_to_another_driver"
    const val SOCKET_CALL_ACCEPT_REQUEST_BOOKNOW = "accept_booking_request"
    const val SOCKET_CALL_ARRIVED_AT_PICKUP_LOCATION = "arrived_at_pickup_location"
    const val SOCKET_CALL_START_TRIP_BOOKNOW = "start_trip"
    const val SOCKET_CALL_ASK_FOR_TIPS = "ask_for_tips"
    const val SOCKET_CALL_ON_THE_WAY_BOOKING_REQUEST = "on_the_way_booking_request"
    const val SOCKET_CALL_WAITING_TIME_ALERT = "waiting_time_alert"
    const val SOCKET_CALL_VERIFY_CUSTOMER = "verify_customer"
    const val SOCKET_CALL_REQUEST_CODE_FOR_COMPLETE_TRIP = "request_code_for_complete_trip"
    const val SOCKET_CALL_TRACK_TRIP = "track_trip"

    const val SOCKET_CALL_START_WAITING_TIME = "start_waiting_time_for_meter"
    const val SOCKET_CALL_END_WAITING_TIME = "end_waiting_time_for_meter"
    const val SOCKET_CALL_UPDATE_METER_INFO = "update_meter_booking_status"


    const val SOCKET_CALL_REJECT_BOOKING_REQUEST_BOOK_LATER =
        "ForwardAdvancedBookingRequestToAnother"
    const val SOCKET_CALL_ACCEPT_REQUEST_BOOKLATER = "AcceptAdvancedBookingRequest"
    const val SOCKET_CALL_START_HOLD_TRIP_BOOK_LATER = "AdvancedBookingStartHoldTrip"
    const val SOCKET_CALL_END_HOLD_TRIP_BOOKNOW = "EndHoldTrip"
    const val SOCKET_CALL_END_HOLD_TRIP_BOOK_LATER = "AdvancedBookingEndHoldTrip"
    const val SOCKET_CALL_PICKUP_PASSENGER_BOOK_LATER = "AdvancedBookingPickupPassenger"
    const val SOCKET_CALL_ARRIVED_CLICK_BOOKNOW = "ArrivedAtPickupLocation"
    const val SOCKET_CALL_ARRIVED_CLICK_BOOKLATER = "AdvanceBookingArrivedAtPickupLocation"
    const val SOCKET_CALL_SEND_MESSAGE = "send_message"

    ////////////////////////////////// Socket Params /////////////////////////////////////////////////
    const val SOCKET_PARAM_DRIVER_ID = "driver_id"
    const val SOCKET_PARAM_DRIVER_LAT = "lat"
    const val SOCKET_PARAM_DRIVER_LONG = "lng"
    const val SOCKET_PARAM_BEARING = "bearing"
    const val SOCKET_PARAM_BOOKING_ID = "booking_id"
    const val SOCKET_PARAM_BOOKING_TYPE = "booking_type"
    const val SOCKET_PARAM_CUSTOMER_ID = "customer_id"
    const val SOCKET_PARAM_WAITING_TIME = "waiting_time"
    const val SOCKET_PARAM_OTP = "otp"


    const val SOCKET_PARAM_PICKUP_PASSENGER = "PickupPassenger"
    const val SOCKET_PARAM_SENDER = "Sender"
    const val SOCKET_PARAM_MESSAGE = "Message"
    const val SOCKET_PARAM_SEND_SENDER_NAME = "driver"
    const val SOCKET_PARAM_APP_VERSION = "Version"
    const val SOCKET_PARAM_DRIVER_TOKEN = "Token"
    const val SOCKET_PARAM_DROP_OFF_COUNT = "DropoffCount"

    const val SOCKET_PARAM_METER_TRIP_TIME = "trip_time"
    const val SOCKET_PARAM_METER_TRIP_DISTANCE = "trip_distance"
    const val SOCKET_PARAM_METER_TRIP_FARE = "fare"


    const val ConnectUser = "connect_user"
    const val ConnectDriver = "connect_driver"
    const val driverId = "driver_id"
    const val bookingId = "booking_id"
    const val bookingType = "booking_type"
    const val updateDriverLocation = "update_driver_location"
    const val forwardBookingRequest = "forward_booking_request"
    const val forwardBookingRequestToAnotherDriver = "forward_booking_request_to_another_driver"
    const val acceptBookingRequest = "accept_booking_request"
    const val startTrip = "start_trip"
    const val liveTracking = "live_tracking"
    const val driverArrived = "driver_arrived"
    const val cancelTrip = "cancel_trip"
    const val verifyCustomer = "accept_booking_request"
    const val arrivedAtPickupLocation = "arrived_at_pickup_location"
    const val sendMessage = "send_message"
    const val heatMap = "heat_map"
    const val tripToDestination = "trip_to_destination"
    const val onTheWayBookingRequest = "on_the_way_booking_request"
    /*sos constant*/
    var SOS = ""
    var initGsonData: InitResponse? = null

    /*ride status*/
    const val STATUS_REQUEST_ACCEPT = "accepted"
    const val STATUS_REQUEST_TRAVELING = "traveling"
    var bookingRequestSlide = 1
    var PIN = ""
    var FlagMapDirectionState = ""

    /*Gps TurnOnoff*/
    var GPS_REQUEST = 1
    var GPS_STATUS = 0
}