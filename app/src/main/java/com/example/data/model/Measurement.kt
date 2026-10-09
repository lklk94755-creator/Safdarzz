package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "measurements",
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
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerId: Long,
    val profileTitle: String = "پیمائش (اصل)", // e.g. شلوار قمیض، کرتا، وغیرہ

    // --- ۱۱ مخصوص پیمائشیں (The 11 Tailor Measurements) ---
    val lambai: String = "",       // ۱. لمبائی (Length)
    val teera: String = "",        // ۲۔ تیرہ (Teera / Shoulder)
    val bazu: String = "",         // ۳. بازو (Bazu / Sleeves)
    val collar: String = "",       // ۴. کالر (Collar / Bain)
    val chhati: String = "",       // ۵. چھاتی (Chhati / Chest)
    val kamar: String = "",        // ۶. کمر (Kamar / Waist)
    val halfChhati: String = "",   // ۷. ہاف چھاتی (Half Chhati)
    val jeb: String = "",          // ۸. جیب (Jeb / Pocket)
    val daman: String = "",        // ۹. دامن (Daman / Ghera)
    val shalwar: String = "",      // ۱۰. شلوار (Shalwar / Trouser)
    val paicha: String = "",       // ۱۱. پانچہ (Paicha / Bottom Opening)

    // --- سلائی کے انداز اور کٹنگ آپشنز (Stitching & Cut Styles) ---
    val collarStyle: String = "بین (Bain)",      // بین, کالر, ہاف بین, اوپن گلا
    val damanStyle: String = "گول دامن (Round)", // گول دامن, چورس دامن
    val sleeveStyle: String = "سادہ بازو (Open)", // سادہ بازو, کف والا, گول کف, کٹ کف
    val pocketStyle: String = "سامنے ۱، سائیڈ ۲", // سامنے ۱، سائیڈ ۲, سامنے ۱ سائیڈ ۱, صرف سائیڈ
    val pattiStyle: String = "بٹن پٹی (Exposed)", // بٹن پٹی, گم پٹی, زپ
    val stitchingStyle: String = "سنگل سلائی",    // سنگل سلائی, ڈبل سلائی, ڈیزائن سلائی
    val shalwarPocket: Boolean = true,          // شلوار میں خفیہ جیب
    val notes: String = "",                     // اضافی نوٹ / کلف یا فالٹ وغیرہ
    val updatedAt: Long = System.currentTimeMillis()
)
