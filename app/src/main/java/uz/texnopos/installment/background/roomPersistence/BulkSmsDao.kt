package uz.texnopos.installment.background.roomPersistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.texnopos.installment.background.data.Client


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 6/28/19}
 */

@Dao
interface BulkSmsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(t: BulkSms): Long

    @Query(value = "update bulk_sms set sms_contacts= :smsContacts where bulk_sms_id= :rowId")
    suspend fun update(smsContacts: List<Client>, rowId: Long)

    @Query(value = "select * from bulk_sms where bulk_sms_id= :rowId")
    suspend fun bulkSmsWithRowId(rowId: Long): BulkSms

    @Query(value = "select * from bulk_sms")
    fun all(): List<BulkSms>
}