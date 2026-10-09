package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerCode: String = "",
    val name: String,
    val phone: String,
    val cityOrAddress: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
