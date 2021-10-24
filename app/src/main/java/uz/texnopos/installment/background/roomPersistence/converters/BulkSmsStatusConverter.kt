package uz.texnopos.installment.background.roomPersistence.converters

import androidx.room.TypeConverter
import uz.texnopos.installment.background.roomPersistence.BulkSmsStatus


object BulkSmsStatusConverter {

    @JvmStatic
    @TypeConverter
    fun toSmsStatus(json : String) : BulkSmsStatus {
        return BulkSmsStatus.valueOf(json)
    }

    @JvmStatic
    @TypeConverter
    fun toString(status : BulkSmsStatus) : String {
        return status.name
    }
}