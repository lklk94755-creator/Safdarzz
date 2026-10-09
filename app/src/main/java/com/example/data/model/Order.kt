package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class OrderStatus(val titleUrdu: String, val titleEng: String) {
    PENDING("نئی بکنگ", "Pending"),
    CUTTING("کٹائی شدہ", "Cutting"),
    STITCHING("سلائی جاری", "Stitching"),
    READY("تیار ہے", "Ready"),
    DELIVERED("ڈیلیور شدہ", "Delivered")
}

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val customerId: Long,
    val customerName: String,
    val customerPhone: String,
    val suitType: String = "شلوار قمیض",
    val suitCount: Int = 1,
    val clothColorAndType: String = "",
    val status: OrderStatus = OrderStatus.PENDING,
    val bookingDate: Long = System.currentTimeMillis(),
    val deliveryDate: Long = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000), // Default 7 days
    val totalAmount: Double = 0.0,
    val advanceAmount: Double = 0.0,
    val balanceAmount: Double = 0.0,
    val measurementSnapshot: String = "", // Formatted snapshot of the 11 measurements
    val notes: String = ""
)
