package uz.texnopos.installment.background.roomPersistence.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import uz.texnopos.installment.background.data.SmsContact

/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 6/28/19}
 */

@UnstableDefault
object CollectionTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toCollectionString(list: List<SmsContact>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToCollection(json: String): List<SmsContact> {
        val type= object :TypeToken<List<SmsContact>>(){}.type
        return Gson().fromJson(json, type)
    }
}