package uz.texnopos.installment.background.workers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import spartons.com.prosmssenderapp.helper.SharedPreferenceHelper
import spartons.com.prosmssenderapp.helper.SharedPreferenceHelper.Companion.BULKS_SMS_PREVIOUS_WORKER_ID
import spartons.com.prosmssenderapp.helper.SharedPreferenceHelper.Companion.BULK_SMS_PREFERRED_CARRIER_NUMBER
import spartons.com.prosmssenderapp.helper.SharedPreferenceHelper.Companion.BULK_SMS_PREFERRED_MULTIPLE_CARRIER_NUMBER_FLAG
import spartons.com.prosmssenderapp.util.Event
import uz.texnopos.installment.App
import uz.texnopos.installment.R
import uz.texnopos.installment.background.data.toSmsContact
import uz.texnopos.installment.background.helper.NotificationIdHelper
import uz.texnopos.installment.background.roomPersistence.BulkSms
import uz.texnopos.installment.background.roomPersistence.BulkSmsDao
import uz.texnopos.installment.background.util.Constants.CARRIER_NAME_SPLITTER
import uz.texnopos.installment.background.util.enqueueWorker
import uz.texnopos.installment.background.util.subscriptionManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 6/26/19}
 */

@SuppressLint("MissingPermission")
class SendBulkSmsViewModel(
    application: App,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val bulkSmsDao: BulkSmsDao,
) : AndroidViewModel(application) {

    private val coroutineContext = Dispatchers.Default + SupervisorJob()

    private val _uiState = MutableLiveData<SendBulkSmsUiModel>()
    val uiState: LiveData<SendBulkSmsUiModel> = _uiState

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun handleDeviceCarrierNumbers() {
        if (sharedPreferenceHelper.getString(BULK_SMS_PREFERRED_CARRIER_NUMBER) != null && sharedPreferenceHelper.getBoolean(
                BULK_SMS_PREFERRED_MULTIPLE_CARRIER_NUMBER_FLAG
            )
        ) return
        val subscriptionManager = getApplication<App>().subscriptionManager
        val allCarrierNumbers = subscriptionManager.activeSubscriptionInfoList.map {
            it.subscriptionId.toString()
                .plus("$CARRIER_NAME_SPLITTER ${it.carrierName}(${it.number})")
        }
        viewModelScope.launch(coroutineContext) {
            when {
                allCarrierNumbers.isEmpty() -> emitUiState(noDeviceNumber = true)
                allCarrierNumbers.count() > 1 -> emitUiState(
                    showMultipleCarrierNumber = Event(allCarrierNumbers)
                )
                else -> sharedPreferenceHelper.put(
                    BULK_SMS_PREFERRED_CARRIER_NUMBER, allCarrierNumbers.component1()
                )
            }
        }
    }

    private suspend fun emitUiState(
        showProgress: Boolean = false, contactList: Event<List<String>>? = null,
        showMessage: Event<Int>? = null, noDeviceNumber: Boolean = false,
        showMultipleCarrierNumber: Event<List<String>>? = null,
    ) = withContext(Dispatchers.Main) {
        SendBulkSmsUiModel(
            showProgress, contactList,
            showMessage, noDeviceNumber, showMultipleCarrierNumber
        ).also {
            _uiState.value = it
        }
    }

    fun handleSelectedFile(selectedFile: File) {
        viewModelScope.launch(coroutineContext) {
            emitUiState(showProgress = true)
            BufferedReader(FileReader(selectedFile)).use {
                val filteredContactList = it.readLines()
                    .filter { contactNumber ->
                        contactNumber.length > 6
                    }
                if (filteredContactList.isNotEmpty())
                    emitUiState(contactList = Event(filteredContactList))
                else
                    emitUiState(showMessage = Event(R.string.the_selected_file_is_empty))
            }
        }
    }

    fun checkIfWorkerIsIdle() =
        sharedPreferenceHelper.getString(BULKS_SMS_PREVIOUS_WORKER_ID) == null

    fun sendBulkSms(contactList: Array<String>, smsContent: String) {
        viewModelScope.launch(coroutineContext) {
            val carrierName =
                sharedPreferenceHelper.getString(BULK_SMS_PREFERRED_CARRIER_NUMBER)?.split(
                    CARRIER_NAME_SPLITTER
                )?.get(1) ?: ""
            val bulkSms = BulkSms(
                smsContacts = contactList.map { it.toSmsContact() }.toList(),
                smsContent = smsContent, startDateTime = System.currentTimeMillis(),
                carrierName = carrierName
            )
            val rowId = bulkSmsDao.insert(bulkSms)
            val worker = getApplication<App>().enqueueWorker<SendBulkSmsWorker> {
                setInputData(
                    SendBulkSmsWorker.constructWorkerParams(
                        rowId, NotificationIdHelper.getId()
                    )
                )
            }
            sharedPreferenceHelper.put(BULKS_SMS_PREVIOUS_WORKER_ID, worker.id.toString())
        }
    }
}

data class SendBulkSmsUiModel(
    val showProgress: Boolean,
    val contactList: Event<List<String>>?,
    val showMessage: Event<Int>?,
    val noDeviceNumber: Boolean,
    val showMultipleCarrierNumber: Event<List<String>>?,
)