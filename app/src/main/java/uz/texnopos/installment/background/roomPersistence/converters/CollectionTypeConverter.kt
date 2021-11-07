package uz.texnopos.installment.background.roomPersistence.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.UnstableDefault
import uz.texnopos.installment.background.data.Client

@UnstableDefault
object CollectionTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toCollectionString(list: List<Client>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToCollection(json: String): List<Client> {
        val type = object : TypeToken<List<Client>>() {}.type
        return Gson().fromJson(json, type)
    }
}