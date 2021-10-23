package uz.texnopos.installment.background.workers

import android.content.Context
import android.provider.Telephony
import android.telephony.SmsManager
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import timber.log.Timber
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.background.data.Client
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.background.util.Constants
import uz.texnopos.installment.background.util.Constants.BULKS_SMS_PREVIOUS_WORKER_ID
import uz.texnopos.installment.background.util.Constants.BULK_SMS_MESSAGE_DELAY_SECONDS
import uz.texnopos.installment.background.util.Constants.BULK_SMS_PREFERRED_CARRIER_NUMBER
import uz.texnopos.installment.core.getSharedPreferences
import java.util.*

class SendBulkSmsWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    private val bulkSmsDao = BulkSmsDatabase.getInstance(getAppInstance()).bulkSmsDao()
    private val subscriptionInfoId =
        getSharedPreferences().getStringValue(BULK_SMS_PREFERRED_CARRIER_NUMBER, "1: UMS()")?.split(
            Constants.CARRIER_NAME_SPLITTER
        )?.component1()?.toInt()

    private val smsManager =
        if (subscriptionInfoId != null) SmsManager.getSmsManagerForSubscriptionId(subscriptionInfoId) else SmsManager.getDefault()
    private val rowId = inputData.getLong(BULK_SMS_ROW_ID, -1)

    companion object {
        private const val BULK_SMS_ROW_ID =
            "uz.texnopos.installment.background.workers.SendBulkSmsWorker.BULK_SMS_ROW_ID"
        private const val SMS_CONTENT_LENGTH_LIMIT = 140

        fun constructWorkerParams(rowId: Long): Data {
            return Data.Builder()
                .putLong(BULK_SMS_ROW_ID, rowId)
                .build()
        }
    }

    fun Client.toSmsText(smsText:String):String{
        return smsText
            .replace("{first_name}", first_name)
            .replace("{last_name}", last_name)
            .replace("{magazin}","2-magazin")
            .replace("{amount}", amount)
            .replace("{end_date}", end_date)
    }

    override suspend fun doWork(): Result {
        Timber.e("Worker starts")
        val bulkSms = bulkSmsDao.bulkSmsWithRowId(rowId)
        val smsContacts = bulkSms.smsContacts
        val remainSmsSentNumbers = smsContacts.filter { !it.isSent1 }
        val smsDelayValue = (getSharedPreferences().getIntValue(BULK_SMS_MESSAGE_DELAY_SECONDS, 30).toLong()) * 1000
        val smsContent = bulkSms.smsContent
        val smsDivides = smsManager.divideMessage(smsContent)
        val sourceSmsAddress = getSharedPreferences().getStringValue(BULK_SMS_PREFERRED_CARRIER_NUMBER, "1: UMS()")
        Timber.e("Source sms address -> $sourceSmsAddress and content -> $smsContent")
        for (smsContact in remainSmsSentNumbers) {
            if (smsContent.length < SMS_CONTENT_LENGTH_LIMIT) {

                smsManager.sendTextMessage(smsContact.phone1 + 1,
                    null,
                    smsContact.toSmsText(smsContent),
                    null,
                    null)
                delay(smsDelayValue)
                smsContact.isSent1 = true

                smsManager.sendTextMessage(smsContact.phone2 + 1,
                    null,
                    smsContact.toSmsText(smsContent),
                    null,
                    null)
                delay(smsDelayValue)
                smsContact.isSent2 = true
            } else {

                smsManager.sendMultipartTextMessage(smsContact.phone1 + 1,
                    null,
                    smsDivides.map { smsContact.toSmsText(it) } as ArrayList<String>,
                    null,
                    null)
                delay(smsDelayValue)
                smsContact.isSent1 = true

                smsManager.sendMultipartTextMessage(smsContact.phone2 + 1,
                    null,
                    smsDivides.map { smsContact.toSmsText(it) } as ArrayList<String>,
                    null,
                    null)
                delay(smsDelayValue)
                smsContact.isSent1 = true
            }
            bulkSmsDao.update(smsContacts, rowId)
        }
        getSharedPreferences().setValue(BULKS_SMS_PREVIOUS_WORKER_ID, "")
        Timber.e("Worker ends")
        return Result.success()
    }
}
