package uz.texnopos.installment.core

object Constants {

    //baseUrl
    const val BASE_URL: String = "https://back-end.i-plan.uz/"

    const val TOKEN = "accessToken"
    const val NO_INTERNET = "Интернет не подключен"
    const val UNAUTHORIZED = "Не зарегистрирован"
    const val mySharedPreferences = "InstallmentPreferences"
    const val CLIENT = "client"
    const val ORDER = "order"

    const val ASK_SMS_PERMISSION_REQUEST_CODE = 1001
    const val ASK_PHONE_PERMISSION_REQUEST_CODE=1002
    const val BULK_SMS_MESSAGE_DELAY_SECONDS = "message_delay_seconds"
    const val BULKS_SMS_PREVIOUS_WORKER_ID = "previous_worker_id"
    const val BULK_SMS_ROW_ID = "BULK_SMS_ROW_ID"
    const val MINUTE = 60 * 1000L

}
