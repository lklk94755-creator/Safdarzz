package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_profile")
data class ShopProfile(
    @PrimaryKey
    val id: Int = 1,
    val shopName: String = "ماسٹر ٹیلرز (Master Tailor)",
    val ownerName: String = "استاد درزی",
    val phone: String = "0300-1234567",
    val address: String = "مین بازار، نزد جامع مسجد",
    val slipFooterNote: String = "شکریہ! تیار سوٹ ڈیلیوری تاریخ سے 15 دن کے اندر وصول فرما لیں۔"
)
