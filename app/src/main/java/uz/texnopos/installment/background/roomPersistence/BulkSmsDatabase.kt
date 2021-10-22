package uz.texnopos.installment.background.roomPersistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.serialization.UnstableDefault
import spartons.com.prosmssenderapp.roomPersistence.converters.BulkSmsStatusConverter
import uz.texnopos.installment.background.roomPersistence.converters.CollectionTypeConverter
import kotlin.OptIn as KotlinOptIn


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 6/28/19}
 */

@Database(entities = [BulkSms::class], version = 1, exportSchema = false)
@TypeConverters(
    CollectionTypeConverter::class,
    BulkSmsStatusConverter::class
)
abstract class BulkSmsDatabase : RoomDatabase() {

    abstract fun bulkSmsDao(): BulkSmsDao

    companion object {

        @Volatile
        private var INSTANCE: BulkSmsDatabase? = null

        fun getInstance(context: Context): BulkSmsDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BulkSmsDatabase::class.java,
                "bulk_sms.db"
            )
                .build()
    }
}