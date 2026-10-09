package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Customer
import com.example.data.model.Measurement
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.ShopProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface TailorDao {

    // --- Customers ---
    @Query("SELECT * FROM customers ORDER BY id DESC")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR customerCode LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchCustomers(query: String): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
    fun getCustomerById(id: Long): Flow<Customer?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT COUNT(*) FROM customers")
    fun getCustomerCount(): Flow<Int>

    // --- Measurements ---
    @Query("SELECT * FROM measurements WHERE customerId = :customerId ORDER BY id DESC LIMIT 1")
    fun getMeasurementForCustomer(customerId: Long): Flow<Measurement?>

    @Query("SELECT * FROM measurements WHERE id = :id LIMIT 1")
    fun getMeasurementById(id: Long): Flow<Measurement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement): Long

    @Update
    suspend fun updateMeasurement(measurement: Measurement)

    @Delete
    suspend fun deleteMeasurement(measurement: Measurement)

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY deliveryDate ASC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY id DESC")
    fun getOrdersForCustomer(customerId: Long): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    fun getOrderById(id: Long): Flow<Order?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("SELECT COUNT(*) FROM orders WHERE status != 'DELIVERED'")
    fun getActiveOrdersCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM orders WHERE status = 'READY'")
    fun getReadyOrdersCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(balanceAmount), 0.0) FROM orders WHERE status != 'DELIVERED'")
    fun getTotalPendingBalance(): Flow<Double>

    // --- Shop Profile ---
    @Query("SELECT * FROM shop_profile WHERE id = 1 LIMIT 1")
    fun getShopProfile(): Flow<ShopProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShopProfile(profile: ShopProfile)
}
