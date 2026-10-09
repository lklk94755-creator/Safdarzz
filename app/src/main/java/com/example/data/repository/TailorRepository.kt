package com.example.data.repository

import com.example.data.local.TailorDao
import com.example.data.model.Customer
import com.example.data.model.Measurement
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.ShopProfile
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TailorRepository(private val dao: TailorDao) {

    // Customers
    val allCustomers: Flow<List<Customer>> = dao.getAllCustomers()
    val customerCount: Flow<Int> = dao.getCustomerCount()

    fun searchCustomers(query: String): Flow<List<Customer>> = dao.searchCustomers(query)
    fun getCustomerById(id: Long): Flow<Customer?> = dao.getCustomerById(id)

    suspend fun insertCustomer(customer: Customer): Long = dao.insertCustomer(customer)
    suspend fun updateCustomer(customer: Customer) = dao.updateCustomer(customer)
    suspend fun deleteCustomer(customer: Customer) = dao.deleteCustomer(customer)

    // Measurements
    fun getMeasurementForCustomer(customerId: Long): Flow<Measurement?> = dao.getMeasurementForCustomer(customerId)
    fun getMeasurementById(id: Long): Flow<Measurement?> = dao.getMeasurementById(id)

    suspend fun saveMeasurement(measurement: Measurement): Long = dao.insertMeasurement(measurement)
    suspend fun deleteMeasurement(measurement: Measurement) = dao.deleteMeasurement(measurement)

    // Orders
    val allOrders: Flow<List<Order>> = dao.getAllOrders()
    val activeOrdersCount: Flow<Int> = dao.getActiveOrdersCount()
    val readyOrdersCount: Flow<Int> = dao.getReadyOrdersCount()
    val totalPendingBalance: Flow<Double> = dao.getTotalPendingBalance()

    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> = dao.getOrdersByStatus(status)
    fun getOrdersForCustomer(customerId: Long): Flow<List<Order>> = dao.getOrdersForCustomer(customerId)
    fun getOrderById(id: Long): Flow<Order?> = dao.getOrderById(id)

    suspend fun insertOrder(order: Order): Long = dao.insertOrder(order)
    suspend fun updateOrder(order: Order) = dao.updateOrder(order)
    suspend fun deleteOrder(order: Order) = dao.deleteOrder(order)

    // Shop Profile
    val shopProfile: Flow<ShopProfile?> = dao.getShopProfile()
    suspend fun saveShopProfile(profile: ShopProfile) = dao.saveShopProfile(profile)

    // Generate formatted measurement summary
    fun formatMeasurementSummary(m: Measurement): String {
        return buildString {
            appendLine("✂️ *پیمائش سلپ (Measurement Details)* ✂️")
            appendLine("۱. لمبائی (Length): ${m.lambai.ifBlank { "-" }}")
            appendLine("۲۔ تیرہ (Teera): ${m.teera.ifBlank { "-" }}")
            appendLine("۳. بازو (Sleeves): ${m.bazu.ifBlank { "-" }}")
            appendLine("۴. کالر (Collar): ${m.collar.ifBlank { "-" }}")
            appendLine("۵. چھاتی (Chest): ${m.chhati.ifBlank { "-" }}")
            appendLine("۶. کمر (Waist): ${m.kamar.ifBlank { "-" }}")
            appendLine("۷. ہاف چھاتی (Half Chest): ${m.halfChhati.ifBlank { "-" }}")
            appendLine("۸. جیب (Pocket): ${m.jeb.ifBlank { "-" }}")
            appendLine("۹. دامن (Daman): ${m.daman.ifBlank { "-" }}")
            appendLine("۱۰. شلوار (Shalwar): ${m.shalwar.ifBlank { "-" }}")
            appendLine("۱۱. پانچہ (Bottom / Paicha): ${m.paicha.ifBlank { "-" }}")
            appendLine("-------------------------")
            appendLine("کالر ڈیزائن: ${m.collarStyle}")
            appendLine("دامن ڈیزائن: ${m.damanStyle}")
            appendLine("بازو / کف: ${m.sleeveStyle}")
            appendLine("جیبیں: ${m.pocketStyle}")
            appendLine("پٹی ڈیزائن: ${m.pattiStyle}")
            if (m.notes.isNotBlank()) {
                appendLine("اضافی ہدایات: ${m.notes}")
            }
        }
    }

    // Generate WhatsApp / Share text slip
    fun generateOrderSlip(order: Order, customer: Customer, measurement: Measurement?, shop: ShopProfile?): String {
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val shopTitle = shop?.shopName ?: "ماسٹر درزی (Master Tailor)"
        val shopPhone = shop?.phone ?: ""
        val shopAddress = shop?.address ?: ""

        return buildString {
            appendLine("===============================")
            appendLine("     🧵 $shopTitle 🧵")
            if (shopAddress.isNotBlank()) appendLine("📍 $shopAddress")
            if (shopPhone.isNotBlank()) appendLine("📞 رابطہ: $shopPhone")
            appendLine("===============================")
            appendLine("📋 آرڈر نمبر: ${order.orderNumber}")
            appendLine("👤 گاہک کا نام: ${customer.name}")
            appendLine("📱 فون نمبر: ${customer.phone}")
            appendLine("📅 بکنگ تاریخ: ${dateFormat.format(Date(order.bookingDate))}")
            appendLine("🎯 ڈیلیوری تاریخ: ${dateFormat.format(Date(order.deliveryDate))}")
            appendLine("🪡 سوٹ کی تعداد: ${order.suitCount} (${order.suitType})")
            if (order.clothColorAndType.isNotBlank()) {
                appendLine("🎨 کپڑا / رنگ: ${order.clothColorAndType}")
            }
            appendLine("-------------------------------")
            if (measurement != null) {
                appendLine("📏 کسٹمر کی پیمائش (11 نکات):")
                appendLine("۱. لمبائی: ${measurement.lambai.ifBlank { "-" }}")
                appendLine("۲۔ تیرہ: ${measurement.teera.ifBlank { "-" }}")
                appendLine("۳. بازو: ${measurement.bazu.ifBlank { "-" }}")
                appendLine("۴. کالر: ${measurement.collar.ifBlank { "-" }}")
                appendLine("۵. چھاتی: ${measurement.chhati.ifBlank { "-" }}")
                appendLine("۶. کمر: ${measurement.kamar.ifBlank { "-" }}")
                appendLine("۷. ہاف چھاتی: ${measurement.halfChhati.ifBlank { "-" }}")
                appendLine("۸. جیب: ${measurement.jeb.ifBlank { "-" }}")
                appendLine("۹. دامن: ${measurement.daman.ifBlank { "-" }}")
                appendLine("۱۰. شلوار: ${measurement.shalwar.ifBlank { "-" }}")
                appendLine("۱۱. پانچہ: ${measurement.paicha.ifBlank { "-" }}")
                appendLine("سٹائل: ${measurement.collarStyle} | ${measurement.damanStyle} | ${measurement.sleeveStyle}")
                appendLine("-------------------------------")
            }
            appendLine("💰 کل رقم: Rs. ${order.totalAmount.toInt()}")
            appendLine("💵 پیشگی رقم (Advance): Rs. ${order.advanceAmount.toInt()}")
            appendLine("⚠️ بقایا رقم (Balance): Rs. ${order.balanceAmount.toInt()}")
            appendLine("حالت: ${order.status.titleUrdu} (${order.status.titleEng})")
            appendLine("===============================")
            appendLine(shop?.slipFooterNote ?: "شکریہ برائے انتخاب!")
        }
    }

    // Seed realistic sample data
    suspend fun seedInitialDataIfEmpty() {
        // Initialize default shop profile
        dao.saveShopProfile(
            ShopProfile(
                id = 1,
                shopName = "ایزی ماسٹر ٹیلرز (Ezy Tailor Master)",
                ownerName = "ماسٹر محمد اسلم",
                phone = "0300-7654321",
                address = "شاہ عالم مارکیٹ، دکان نمبر 14، لاہور",
                slipFooterNote = "معزز گاہک! سوٹ وصول کرتے وقت سلائی ضرور چیک فرما لیں۔ شکریہ!"
            )
        )

        // Add 2 realistic customers with measurements and orders
        val cust1Id = dao.insertCustomer(
            Customer(
                customerCode = "ET-101",
                name = "حاجی بلال احمد",
                phone = "0302-8889911",
                cityOrAddress = "اقبال ٹاؤن، لاہور",
                notes = "معیاری فائن سلائی پسند ہے، کالر پر کلف لازمی ہے۔"
            )
        )

        dao.insertMeasurement(
            Measurement(
                customerId = cust1Id,
                profileTitle = "شلوار قمیض کلاسک",
                lambai = "40.5",
                teera = "18.5",
                bazu = "24.0",
                collar = "15.5",
                chhati = "38.0",
                kamar = "36.0",
                halfChhati = "19.5",
                jeb = "5.5",
                daman = "23.5",
                shalwar = "39.0",
                paicha = "8.0",
                collarStyle = "بین (Bain)",
                damanStyle = "گول دامن (Round)",
                sleeveStyle = "کف (Cuff)",
                pocketStyle = "سامنے ۱، سائیڈ ۲",
                pattiStyle = "گم پٹی (Concealed)",
                notes = "ہلکا کلف، بٹن کالے دھاگے سے ٹانکے جائیں۔"
            )
        )

        dao.insertOrder(
            Order(
                orderNumber = "ORD-1001",
                customerId = cust1Id,
                customerName = "حاجی بلال احمد",
                customerPhone = "0302-8889911",
                suitType = "شلوار قمیض",
                suitCount = 2,
                clothColorAndType = "سفید کاٹن لٹھا اور گرے واش اینڈ وئیر",
                status = OrderStatus.CUTTING,
                bookingDate = System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000),
                deliveryDate = System.currentTimeMillis() + (3L * 24 * 60 * 60 * 1000),
                totalAmount = 3000.0,
                advanceAmount = 1000.0,
                balanceAmount = 2000.0,
                notes = "جمعہ سے پہلے تیار کرنا ہے۔"
            )
        )

        val cust2Id = dao.insertCustomer(
            Customer(
                customerCode = "ET-102",
                name = "چوہدری رضوان علی",
                phone = "0333-4455667",
                cityOrAddress = "جوہر ٹاؤن، لاہور",
                notes = "نارمل ڈھیلا کرتا پاجامہ"
            )
        )

        dao.insertMeasurement(
            Measurement(
                customerId = cust2Id,
                profileTitle = "کرتا پاجامہ",
                lambai = "42.0",
                teera = "19.0",
                bazu = "24.5",
                collar = "16.0",
                chhati = "40.0",
                kamar = "38.5",
                halfChhati = "20.5",
                jeb = "6.0",
                daman = "24.5",
                shalwar = "40.0",
                paicha = "8.5",
                collarStyle = "کالر (Standard)",
                damanStyle = "چورس دامن (Square)",
                sleeveStyle = "سادہ بازو (Open)",
                pocketStyle = "سامنے ۱، سائیڈ ۲",
                pattiStyle = "بٹن پٹی (Exposed)",
                notes = "سائیڈ جیب موبائل سائز کی ہونی چاہیے۔"
            )
        )

        dao.insertOrder(
            Order(
                orderNumber = "ORD-1002",
                customerId = cust2Id,
                customerName = "چوہدری رضوان علی",
                customerPhone = "0333-4455667",
                suitType = "کرتا پاجامہ",
                suitCount = 1,
                clothColorAndType = "سیاہ کڑھائی والا بوسکی",
                status = OrderStatus.READY,
                bookingDate = System.currentTimeMillis() - (5L * 24 * 60 * 60 * 1000),
                deliveryDate = System.currentTimeMillis() + (1L * 24 * 60 * 60 * 1000),
                totalAmount = 1800.0,
                advanceAmount = 1800.0,
                balanceAmount = 0.0,
                notes = "پیکنگ کر کے رکھ دیا ہے۔"
            )
        )
    }
}
