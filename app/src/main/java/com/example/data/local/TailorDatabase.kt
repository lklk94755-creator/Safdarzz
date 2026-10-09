package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.Customer
import com.example.data.model.Measurement
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.ShopProfile

class OrderStatusConverter {
    @TypeConverter
    fun fromStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): OrderStatus = try {
        OrderStatus.valueOf(value)
    } catch (e: Exception) {
        OrderStatus.PENDING
    }
}

@Database(
    entities = [
        Customer::class,
        Measurement::class,
        Order::class,
        ShopProfile::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrderStatusConverter::class)
abstract class TailorDatabase : RoomDatabase() {
    abstract fun tailorDao(): TailorDao

    companion object {
        @Volatile
        private var INSTANCE: TailorDatabase? = null

        fun getDatabase(context: Context): TailorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TailorDatabase::class.java,
                    "tailor_master.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
