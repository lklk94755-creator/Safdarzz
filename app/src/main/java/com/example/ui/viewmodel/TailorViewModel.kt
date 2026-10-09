package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TailorDatabase
import com.example.data.model.Customer
import com.example.data.model.Measurement
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.ShopProfile
import com.example.data.repository.TailorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TailorStats(
    val totalCustomers: Int = 0,
    val activeOrders: Int = 0,
    val readyOrders: Int = 0,
    val totalPendingBalance: Double = 0.0
)

class TailorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TailorRepository
    init {
        val db = TailorDatabase.getDatabase(application)
        repository = TailorRepository(db.tailorDao())
        viewModelScope.launch {
            val count = repository.customerCount.firstOrNull() ?: 0
            if (count == 0) {
                repository.seedInitialDataIfEmpty()
            }
        }
    }

    // Navigation and tab state
    private val _selectedTab = MutableStateFlow(0) // 0: Dashboard, 1: Customers, 2: Orders, 3: Settings
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Order status filter in Orders tab
    private val _orderFilter = MutableStateFlow<OrderStatus?>(null) // null means All
    val orderFilter: StateFlow<OrderStatus?> = _orderFilter.asStateFlow()

    fun setOrderFilter(status: OrderStatus?) {
        _orderFilter.value = status
    }

    // Customer stream
    val allCustomers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered customers based on search query
    val filteredCustomers: StateFlow<List<Customer>> = combine(allCustomers, _searchQuery) { list, query ->
        if (query.isBlank()) list
        else list.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.phone.contains(query, ignoreCase = true) ||
            it.customerCode.contains(query, ignoreCase = true) ||
            it.cityOrAddress.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All orders stream
    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered orders based on status & search
    val filteredOrders: StateFlow<List<Order>> = combine(allOrders, _orderFilter, _searchQuery) { orders, filter, query ->
        orders.filter { order ->
            val matchesFilter = filter == null || order.status == filter
            val matchesQuery = query.isBlank() ||
                order.customerName.contains(query, ignoreCase = true) ||
                order.orderNumber.contains(query, ignoreCase = true) ||
                order.customerPhone.contains(query, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Stats
    val stats: StateFlow<TailorStats> = combine(
        repository.customerCount,
        repository.activeOrdersCount,
        repository.readyOrdersCount,
        repository.totalPendingBalance
    ) { customers, active, ready, balance ->
        TailorStats(
            totalCustomers = customers,
            activeOrders = active,
            readyOrders = ready,
            totalPendingBalance = balance
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TailorStats())

    // Shop Profile
    val shopProfile: StateFlow<ShopProfile?> = repository.shopProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current Customer / Measurement Edit Selection
    private val _currentCustomer = MutableStateFlow<Customer?>(null)
    val currentCustomer: StateFlow<Customer?> = _currentCustomer.asStateFlow()

    private val _currentMeasurement = MutableStateFlow<Measurement?>(null)
    val currentMeasurement: StateFlow<Measurement?> = _currentMeasurement.asStateFlow()

    // Active order for viewing slip / details
    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder.asStateFlow()

    // Dialog states
    private val _showAddCustomerDialog = MutableStateFlow(false)
    val showAddCustomerDialog: StateFlow<Boolean> = _showAddCustomerDialog.asStateFlow()

    private val _showAddOrderDialog = MutableStateFlow(false)
    val showAddOrderDialog: StateFlow<Boolean> = _showAddOrderDialog.asStateFlow()

    private val _showSlipDialog = MutableStateFlow(false)
    val showSlipDialog: StateFlow<Boolean> = _showSlipDialog.asStateFlow()

    private val _showMeasurementEditor = MutableStateFlow(false)
    val showMeasurementEditor: StateFlow<Boolean> = _showMeasurementEditor.asStateFlow()

    fun openAddCustomer() {
        _showAddCustomerDialog.value = true
    }

    fun closeAddCustomer() {
        _showAddCustomerDialog.value = false
    }

    fun openAddOrder(forCustomer: Customer? = null) {
        if (forCustomer != null) {
            _currentCustomer.value = forCustomer
            loadCustomerMeasurement(forCustomer.id)
        }
        _showAddOrderDialog.value = true
    }

    fun closeAddOrder() {
        _showAddOrderDialog.value = false
    }

    fun openSlipDialog(order: Order) {
        _selectedOrder.value = order
        viewModelScope.launch {
            val customer = repository.getCustomerById(order.customerId).firstOrNull()
            _currentCustomer.value = customer
            if (customer != null) {
                val measurement = repository.getMeasurementForCustomer(customer.id).firstOrNull()
                _currentMeasurement.value = measurement
            }
            _showSlipDialog.value = true
        }
    }

    fun closeSlipDialog() {
        _showSlipDialog.value = false
    }

    fun selectCustomer(customer: Customer) {
        _currentCustomer.value = customer
        loadCustomerMeasurement(customer.id)
    }

    fun openMeasurementEditor(customer: Customer) {
        _currentCustomer.value = customer
        loadCustomerMeasurement(customer.id)
        _showMeasurementEditor.value = true
    }

    fun closeMeasurementEditor() {
        _showMeasurementEditor.value = false
    }

    private fun loadCustomerMeasurement(customerId: Long) {
        viewModelScope.launch {
            val m = repository.getMeasurementForCustomer(customerId).firstOrNull()
            _currentMeasurement.value = m ?: Measurement(customerId = customerId)
        }
    }

    // CRUD operations
    fun saveCustomer(name: String, phone: String, address: String, notes: String, existingId: Long? = null) {
        viewModelScope.launch {
            val code = "ET-" + ((100..999).random())
            val customer = Customer(
                id = existingId ?: 0,
                customerCode = code,
                name = name.trim(),
                phone = phone.trim(),
                cityOrAddress = address.trim(),
                notes = notes.trim()
            )
            val newId = if (existingId == null || existingId == 0L) {
                repository.insertCustomer(customer)
            } else {
                repository.updateCustomer(customer)
                existingId
            }
            _currentCustomer.value = customer.copy(id = newId)
            closeAddCustomer()
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
            if (_currentCustomer.value?.id == customer.id) {
                _currentCustomer.value = null
                _currentMeasurement.value = null
            }
        }
    }

    fun saveMeasurement(measurement: Measurement) {
        viewModelScope.launch {
            val id = repository.saveMeasurement(measurement)
            _currentMeasurement.value = measurement.copy(id = id)
            closeMeasurementEditor()
        }
    }

    fun createOrder(
        customer: Customer,
        suitType: String,
        suitCount: Int,
        clothDetails: String,
        deliveryTimestamp: Long,
        totalAmount: Double,
        advanceAmount: Double,
        notes: String
    ) {
        viewModelScope.launch {
            val orderNum = "ORD-${(1000..9999).random()}"
            val measurement = repository.getMeasurementForCustomer(customer.id).firstOrNull()
            val snapshot = if (measurement != null) repository.formatMeasurementSummary(measurement) else ""
            val balance = (totalAmount - advanceAmount).coerceAtLeast(0.0)

            val newOrder = Order(
                orderNumber = orderNum,
                customerId = customer.id,
                customerName = customer.name,
                customerPhone = customer.phone,
                suitType = suitType,
                suitCount = suitCount,
                clothColorAndType = clothDetails,
                status = OrderStatus.PENDING,
                bookingDate = System.currentTimeMillis(),
                deliveryDate = deliveryTimestamp,
                totalAmount = totalAmount,
                advanceAmount = advanceAmount,
                balanceAmount = balance,
                measurementSnapshot = snapshot,
                notes = notes
            )
            repository.insertOrder(newOrder)
            closeAddOrder()
        }
    }

    fun updateOrderStatus(order: Order, newStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(status = newStatus))
            if (_selectedOrder.value?.id == order.id) {
                _selectedOrder.value = _selectedOrder.value?.copy(status = newStatus)
            }
        }
    }

    fun updateOrderPayment(order: Order, newAdvance: Double) {
        viewModelScope.launch {
            val newBalance = (order.totalAmount - newAdvance).coerceAtLeast(0.0)
            repository.updateOrder(order.copy(advanceAmount = newAdvance, balanceAmount = newBalance))
            if (_selectedOrder.value?.id == order.id) {
                _selectedOrder.value = _selectedOrder.value?.copy(advanceAmount = newAdvance, balanceAmount = newBalance)
            }
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            repository.deleteOrder(order)
            if (_selectedOrder.value?.id == order.id) {
                _selectedOrder.value = null
            }
        }
    }

    fun saveShopProfile(profile: ShopProfile) {
        viewModelScope.launch {
            repository.saveShopProfile(profile)
        }
    }

    fun loadSampleData() {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun generateSlipText(order: Order, customer: Customer?, measurement: Measurement?, shop: ShopProfile?): String {
        val safeCustomer = customer ?: Customer(name = order.customerName, phone = order.customerPhone)
        return repository.generateOrderSlip(order, safeCustomer, measurement, shop)
    }
}
