package uz.texnopos.installment.background.workers

import android.annotation.SuppressLint
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uz.texnopos.installment.App
import uz.texnopos.installment.background.data.Client
import uz.texnopos.installment.background.roomPersistence.BulkSms
import uz.texnopos.installment.background.roomPersistence.BulkSmsDao
import uz.texnopos.installment.background.util.enqueueWorker
import uz.texnopos.installment.core.preferences.getSharedPreferences
import uz.texnopos.installment.core.toSmsContact
import uz.texnopos.installment.settings.Constants.BULKS_SMS_PREVIOUS_WORKER_ID

@SuppressLint("MissingPermission")
class SendBulkSmsViewModel(
    application: App,
    private val bulkSmsDao: BulkSmsDao,
) : AndroidViewModel(application) {

    private val coroutineContext = Dispatchers.Default + SupervisorJob()

    fun sendBulkSms(contactList: ArrayList<Client>, smsContent: String) {
        viewModelScope.launch(coroutineContext) {
            val bulkSms = BulkSms(
                smsContacts = contactList.map { it.toSmsContact() }.toList(),
                smsContent = smsContent)
            val rowId = bulkSmsDao.insert(bulkSms)
            val worker = getApplication<App>().enqueueWorker<SendBulkSmsWorker> {
                setInputData(SendBulkSmsWorker.constructWorkerParams(rowId))
            }
            getSharedPreferences().setValue(BULKS_SMS_PREVIOUS_WORKER_ID, worker.id.toString())
        }
    }
}