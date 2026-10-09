package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Customer
import com.example.ui.components.CustomerDetailDialog
import com.example.ui.components.CustomerDialog
import com.example.ui.components.OrderDialog
import com.example.ui.components.OrderSlipDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.CustomersScreen
import com.example.ui.screens.MeasurementEditScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ShopSettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TailorGold
import com.example.ui.theme.TailorNavy
import com.example.ui.viewmodel.TailorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TailorViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TailorApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailorApp(viewModel: TailorViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val orderFilter by viewModel.orderFilter.collectAsStateWithLifecycle()

    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val shopProfile by viewModel.shopProfile.collectAsStateWithLifecycle()
    val filteredCustomers by viewModel.filteredCustomers.collectAsStateWithLifecycle()
    val filteredOrders by viewModel.filteredOrders.collectAsStateWithLifecycle()
    val allCustomers by viewModel.allCustomers.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()

    val currentCustomer by viewModel.currentCustomer.collectAsStateWithLifecycle()
    val currentMeasurement by viewModel.currentMeasurement.collectAsStateWithLifecycle()
    val selectedOrder by viewModel.selectedOrder.collectAsStateWithLifecycle()

    val showAddCustomerDialog by viewModel.showAddCustomerDialog.collectAsStateWithLifecycle()
    val showAddOrderDialog by viewModel.showAddOrderDialog.collectAsStateWithLifecycle()
    val showSlipDialog by viewModel.showSlipDialog.collectAsStateWithLifecycle()
    val showMeasurementEditor by viewModel.showMeasurementEditor.collectAsStateWithLifecycle()

    var showCustomerDetailDialog by remember { mutableStateOf(false) }

    // If measurement editor is open, show the full screen measurement flow
    if (showMeasurementEditor && currentCustomer != null) {
        MeasurementEditScreen(
            customer = currentCustomer!!,
            initialMeasurement = currentMeasurement,
            onSave = { updatedMeasurement ->
                viewModel.saveMeasurement(updatedMeasurement)
            },
            onBack = {
                viewModel.closeMeasurementEditor()
            }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            0 -> shopProfile?.shopName ?: "ایزی ماسٹر ٹیلرز"
                            1 -> "گاہکوں کا رجسٹر (Customers)"
                            2 -> "آرڈرز مینجمنٹ (Orders)"
                            else -> "دکان کی سیٹنگز (Shop Settings)"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                // Tab 0: Dashboard
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = { Text("ڈیش بورڈ", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_dashboard")
                )

                // Tab 1: Customers
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.Group else Icons.Outlined.Group,
                            contentDescription = "Customers"
                        )
                    },
                    label = { Text("گاہک (11 پیمائش)", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_customers")
                )

                // Tab 2: Orders
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                            contentDescription = "Orders"
                        )
                    },
                    label = { Text("آرڈرز و سلپ", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_orders")
                )

                // Tab 3: Settings
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 3) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text("سیٹنگز (مفت)", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Crossfade(targetState = selectedTab, label = "tab_fade") { tab ->
                when (tab) {
                    0 -> DashboardScreen(
                        stats = stats,
                        shopProfile = shopProfile,
                        recentOrders = allOrders.take(5),
                        onNewCustomerClick = { viewModel.openAddCustomer() },
                        onNewOrderClick = { viewModel.openAddOrder() },
                        onViewCustomersClick = { viewModel.selectTab(1) },
                        onOrderStatusChange = { order, newStatus ->
                            viewModel.updateOrderStatus(order, newStatus)
                        },
                        onViewSlip = { order ->
                            viewModel.openSlipDialog(order)
                        },
                        onOrderClick = { order ->
                            viewModel.openSlipDialog(order)
                        }
                    )

                    1 -> CustomersScreen(
                        customers = filteredCustomers,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                        onAddCustomerClick = { viewModel.openAddCustomer() },
                        onEditMeasurementClick = { cust ->
                            viewModel.openMeasurementEditor(cust)
                        },
                        onNewOrderClick = { cust ->
                            viewModel.openAddOrder(cust)
                        },
                        onCustomerClick = { cust ->
                            viewModel.selectCustomer(cust)
                            showCustomerDetailDialog = true
                        }
                    )

                    2 -> OrdersScreen(
                        orders = filteredOrders,
                        activeFilter = orderFilter,
                        onFilterChange = { viewModel.setOrderFilter(it) },
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                        onNewOrderClick = { viewModel.openAddOrder() },
                        onStatusChange = { order, newStatus ->
                            viewModel.updateOrderStatus(order, newStatus)
                        },
                        onViewSlip = { order ->
                            viewModel.openSlipDialog(order)
                        },
                        onOrderClick = { order ->
                            viewModel.openSlipDialog(order)
                        }
                    )

                    3 -> ShopSettingsScreen(
                        currentShopProfile = shopProfile,
                        onSaveProfile = { profile ->
                            viewModel.saveShopProfile(profile)
                        },
                        onLoadSampleData = {
                            viewModel.loadSampleData()
                        }
                    )
                }
            }
        }
    }

    // Dialogs:

    // 1. Add Customer Dialog
    if (showAddCustomerDialog) {
        CustomerDialog(
            onSave = { name, phone, address, notes ->
                viewModel.saveCustomer(name, phone, address, notes)
            },
            onDismiss = {
                viewModel.closeAddCustomer()
            }
        )
    }

    // 2. Add Order Dialog
    if (showAddOrderDialog) {
        OrderDialog(
            customers = allCustomers,
            initialCustomer = currentCustomer,
            onSave = { customer, suitType, suitCount, clothDetails, deliveryTimestamp, totalAmount, advanceAmount, notes ->
                viewModel.createOrder(
                    customer = customer,
                    suitType = suitType,
                    suitCount = suitCount,
                    clothDetails = clothDetails,
                    deliveryTimestamp = deliveryTimestamp,
                    totalAmount = totalAmount,
                    advanceAmount = advanceAmount,
                    notes = notes
                )
            },
            onDismiss = {
                viewModel.closeAddOrder()
            }
        )
    }

    // 3. Order & Measurement Slip Dialog (WhatsApp sharing / print)
    if (showSlipDialog && selectedOrder != null) {
        val slipText = viewModel.generateSlipText(
            order = selectedOrder!!,
            customer = currentCustomer,
            measurement = currentMeasurement,
            shop = shopProfile
        )
        OrderSlipDialog(
            order = selectedOrder!!,
            customer = currentCustomer,
            measurement = currentMeasurement,
            shopProfile = shopProfile,
            slipText = slipText,
            onDismiss = {
                viewModel.closeSlipDialog()
            }
        )
    }

    // 4. Customer Detail & Measurements Dialog
    if (showCustomerDetailDialog && currentCustomer != null) {
        CustomerDetailDialog(
            customer = currentCustomer!!,
            measurement = currentMeasurement,
            onEditMeasurement = {
                showCustomerDetailDialog = false
                viewModel.openMeasurementEditor(currentCustomer!!)
            },
            onNewOrder = {
                showCustomerDetailDialog = false
                viewModel.openAddOrder(currentCustomer!!)
            },
            onDeleteCustomer = {
                viewModel.deleteCustomer(currentCustomer!!)
                showCustomerDetailDialog = false
            },
            onDismiss = {
                showCustomerDetailDialog = false
            }
        )
    }
}
