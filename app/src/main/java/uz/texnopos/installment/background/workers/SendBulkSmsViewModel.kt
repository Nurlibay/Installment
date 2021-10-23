package uz.texnopos.installment.background.workers

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.texnopos.installment.App
import uz.texnopos.installment.background.data.Client
import uz.texnopos.installment.background.data.toSmsContact
import uz.texnopos.installment.background.roomPersistence.BulkSms
import uz.texnopos.installment.background.roomPersistence.BulkSmsDao
import uz.texnopos.installment.background.util.Constants.BULKS_SMS_PREVIOUS_WORKER_ID
import uz.texnopos.installment.background.util.Constants.BULK_SMS_PREFERRED_CARRIER_NUMBER
import uz.texnopos.installment.background.util.Constants.CARRIER_NAME_SPLITTER
import uz.texnopos.installment.background.util.enqueueWorker
import uz.texnopos.installment.core.getSharedPreferences
import uz.texnopos.installment.settings.Constants

@SuppressLint("MissingPermission")
class SendBulkSmsViewModel(
    application: App,
    private val bulkSmsDao: BulkSmsDao,
) : AndroidViewModel(application) {

    private val coroutineContext = Dispatchers.Default + SupervisorJob()

    fun sendBulkSms(contactList: ArrayList<Client>, smsContent: String) {
        viewModelScope.launch(coroutineContext) {
            val carrierName =
                getSharedPreferences().getStringValue(BULK_SMS_PREFERRED_CARRIER_NUMBER,"1: UMS()")?.split(
                    CARRIER_NAME_SPLITTER
                )?.get(1) ?: ""
            Timber.d(carrierName)
            val bulkSms = BulkSms(
                smsContacts = contactList.map { it.toSmsContact() }.toList(),
                smsContent = smsContent,
                carrierName = carrierName
            )
            val rowId = bulkSmsDao.insert(bulkSms)
            val worker = getApplication<App>().enqueueWorker<SendBulkSmsWorker> {
                setInputData(SendBulkSmsWorker.constructWorkerParams(rowId))
            }
            getSharedPreferences().setValue(BULKS_SMS_PREVIOUS_WORKER_ID, worker.id.toString())
        }
    }
}