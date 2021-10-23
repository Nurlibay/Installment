package uz.texnopos.installment.background.roomPersistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.texnopos.installment.background.data.Client


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 6/28/19}
 */

@Entity(tableName = "bulk_sms")
class BulkSms(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bulk_sms_id")
    val id: Long = 0,
    @ColumnInfo(name = "sms_contacts")
    val smsContacts: List<Client>,
    @ColumnInfo(name = "sms_content")
    val smsContent: String,
    @ColumnInfo(name = "carrier_name")
    val carrierName: String,
    @ColumnInfo(name = "status")
    val bulkSmsStatus: BulkSmsStatus = BulkSmsStatus.IN_PROGRESS
) {
    override fun toString(): String {
        return "BulkSms(id=$id, smsContacts=$smsContacts, smsContent='$smsContent', carrierName='$carrierName', bulkSmsStatus=$bulkSmsStatus)"
    }
}