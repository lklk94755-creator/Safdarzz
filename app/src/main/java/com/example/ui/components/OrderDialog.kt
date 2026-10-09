package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Customer
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TailorGold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDialog(
    customers: List<Customer>,
    initialCustomer: Customer? = null,
    onSave: (
        customer: Customer,
        suitType: String,
        suitCount: Int,
        clothDetails: String,
        deliveryTimestamp: Long,
        totalAmount: Double,
        advanceAmount: Double,
        notes: String
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCustomer by remember {
        mutableStateOf(initialCustomer ?: customers.firstOrNull())
    }
    var customerDropdownExpanded by remember { mutableStateOf(false) }

    var suitType by remember { mutableStateOf("شلوار قمیض") }
    var suitCount by remember { mutableIntStateOf(1) }
    var clothDetails by remember { mutableStateOf("") }

    var daysUntilDelivery by remember { mutableIntStateOf(7) }
    val deliveryTimestamp = remember(daysUntilDelivery) {
        System.currentTimeMillis() + (daysUntilDelivery.toLong() * 24 * 60 * 60 * 1000)
    }

    var totalAmountStr by remember { mutableStateOf("1500") }
    var advanceAmountStr by remember { mutableStateOf("500") }
    var notes by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }

    val totalAmount = totalAmountStr.toDoubleOrNull() ?: 0.0
    val advanceAmount = advanceAmountStr.toDoubleOrNull() ?: 0.0
    val balanceAmount = (totalAmount - advanceAmount).coerceAtLeast(0.0)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("order_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "🧵 نیا سوٹ آرڈر بک کریں (New Order)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Select Customer dropdown if multiple
                if (customers.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = customerDropdownExpanded,
                        onExpandedChange = { customerDropdownExpanded = !customerDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCustomer?.let { "${it.name} (${it.phone})" } ?: "گاہک منتخب کریں",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("گاہک (Customer)*") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("select_customer_dropdown"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = customerDropdownExpanded,
                            onDismissRequest = { customerDropdownExpanded = false }
                        ) {
                            customers.forEach { cust ->
                                DropdownMenuItem(
                                    text = { Text("${cust.name} - ${cust.phone}") },
                                    onClick = {
                                        selectedCustomer = cust
                                        customerDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Suit Type chips
                Text(
                    text = "لباس کی قسم (Suit Type):",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("شلوار قمیض", "کرتا پاجامہ", "واسکٹ", "پینٹ شرٹ").forEach { type ->
                        FilterChip(
                            selected = suitType == type,
                            onClick = { suitType = type },
                            label = { Text(type, fontSize = 11.sp) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Suits count & Cloth details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = suitCount.toString(),
                        onValueChange = { input ->
                            val num = input.toIntOrNull()
                            if (num != null && num in 1..99) {
                                suitCount = num
                            }
                        },
                        label = { Text("سوٹ کی تعداد") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = clothDetails,
                        onValueChange = { clothDetails = it },
                        label = { Text("کپڑے کا رنگ و نوعیت") },
                        placeholder = { Text("سفید لٹھا، وغیرہ") },
                        modifier = Modifier.weight(1.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Delivery date selection
                Text(
                    text = "تاریخ واپسی (Delivery Date): ${dateFormat.format(Date(deliveryTimestamp))}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(3 to "3 دن (Urgent)", 5 to "5 دن", 7 to "7 دن (1 ہفتہ)", 14 to "14 دن (2 ہفتے)").forEach { (days, label) ->
                        FilterChip(
                            selected = daysUntilDelivery == days,
                            onClick = { daysUntilDelivery = days },
                            label = { Text(label, fontSize = 11.sp) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Pricing and Payments
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = totalAmountStr,
                        onValueChange = { totalAmountStr = it },
                        label = { Text("کل رقم (Total Rs.)*") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = advanceAmountStr,
                        onValueChange = { advanceAmountStr = it },
                        label = { Text("ایڈوانس (Advance)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calculated Balance Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "بقایا رقم (Balance Due):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Rs. ${balanceAmount.toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (balanceAmount > 0) TailorGold else EmeraldGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("اضافی نوٹ (Order Notes)") },
                    placeholder = { Text("سلائی کی خاص ہدایات") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("منسوخ")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            selectedCustomer?.let { cust ->
                                onSave(
                                    cust,
                                    suitType,
                                    suitCount,
                                    clothDetails,
                                    deliveryTimestamp,
                                    totalAmount,
                                    advanceAmount,
                                    notes
                                )
                            }
                        },
                        enabled = selectedCustomer != null && totalAmount > 0,
                        modifier = Modifier.testTag("submit_order_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("بکنگ مکمل کریں (Book)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
