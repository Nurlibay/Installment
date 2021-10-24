package uz.texnopos.installment.background.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import timber.log.Timber
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.background.data.Phone
import uz.texnopos.installment.background.data.Phones
import uz.texnopos.installment.background.retrofit.RestApi
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.core.*
import uz.texnopos.installment.settings.Constants.BULKS_SMS_PREVIOUS_WORKER_ID
import uz.texnopos.installment.settings.Constants.BULK_SMS_ROW_ID

class SendBulkSmsWorker(val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    private val bulkSmsDao = BulkSmsDatabase.getInstance(getAppInstance()).bulkSmsDao()
    private val rowId = inputData.getLong(BULK_SMS_ROW_ID, -1)
    private val api = RestApi.create()

    override suspend fun doWork(): Result {
        Timber.e("Worker starts")
        val bulkSms = bulkSmsDao.bulkSmsWithRowId(rowId)
        val smsContacts = bulkSms.smsContacts
        val smsContent = bulkSms.smsContent
        for (smsContact in smsContacts) {

            smsContact.isSent1 = SMSUtils.sendSMS(context,
                smsContact.phone1,
                smsContact.toSmsText(smsContent))
            delay(smsDelayValue)

            smsContact.isSent2 = SMSUtils.sendSMS(context,
                smsContact.phone2,
                smsContact.toSmsText(smsContent))
            delay(smsDelayValue)

            bulkSmsDao.update(smsContacts, rowId)
        }
        val phones = arrayListOf<Phone>()
        bulkSmsDao.bulkSmsWithRowId(rowId).smsContacts.forEach {
            if (it.isSent1 || it.isSent2) phones.add(Phone(it.phone1, it.phone2))
        }
        callApi(api.sendDelivered(Phones(phones)),
            {
                if (it != null) context.toast(it.message)
            },
            {
                context.toast(it)
            })
        getSharedPreferences().setValue(BULKS_SMS_PREVIOUS_WORKER_ID, "")
        Timber.e("Worker ends")
        return Result.success()
    }

    companion object {
        fun constructWorkerParams(rowId: Long): Data {
            return Data.Builder()
                .putLong(BULK_SMS_ROW_ID, rowId)
                .build()
        }
    }
}
