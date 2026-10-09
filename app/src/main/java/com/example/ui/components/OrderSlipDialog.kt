package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Customer
import com.example.data.model.Measurement
import com.example.data.model.Order
import com.example.data.model.ShopProfile
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TailorGold
import com.example.ui.theme.TailorNavy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderSlipDialog(
    order: Order,
    customer: Customer?,
    measurement: Measurement?,
    shopProfile: ShopProfile?,
    slipText: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 680.dp)
                .clip(RoundedCornerShape(20.dp))
                .testTag("order_slip_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📋 آرڈر اور پیمائش سلپ (Slip)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Printable Slip content scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFCFDFE))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    // Shop Banner in slip
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = shopProfile?.shopName ?: "ماسٹر ٹیلرز (Master Tailor)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TailorNavy
                            ),
                            textAlign = TextAlign.Center
                        )
                        if (!shopProfile?.address.isNullOrBlank()) {
                            Text(
                                text = shopProfile?.address ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (!shopProfile?.phone.isNullOrBlank()) {
                            Text(
                                text = "رابطہ: ${shopProfile?.phone}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Slip details row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("آرڈر نمبر: ${order.orderNumber}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("گاہک: ${customer?.name ?: order.customerName}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("فون: ${customer?.phone ?: order.customerPhone}", fontSize = 12.sp, color = Color.DarkGray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("تاریخ: ${dateFormat.format(Date(order.bookingDate))}", fontSize = 11.sp, color = Color.Gray)
                            Text("ڈیلیوری: ${dateFormat.format(Date(order.deliveryDate))}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TailorNavy)
                            Text("${order.suitCount} سوٹ (${order.suitType})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (order.clothColorAndType.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "کپڑا: ${order.clothColorAndType}",
                            fontSize = 12.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 11 Custom Measurements Table
                    Text(
                        text = "📏 پیمائش کے ۱۱ مخصوص نکات (Measurements):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TailorNavy
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            // Row 1: لمبائی, تیرہ, بازو
                            SlipMeasureRow("۱. لمبائی", measurement?.lambai, "۲۔ تیرہ", measurement?.teera, "۳. بازو", measurement?.bazu)
                            Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFF1F5F9))
                            // Row 2: کالر, چھاتی, کمر
                            SlipMeasureRow("۴. کالر", measurement?.collar, "۵. چھاتی", measurement?.chhati, "۶. کمر", measurement?.kamar)
                            Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFF1F5F9))
                            // Row 3: ہاف چھاتی, جیب, دامن
                            SlipMeasureRow("۷. ہاف چھاتی", measurement?.halfChhati, "۸. جیب", measurement?.jeb, "۹. دامن", measurement?.daman)
                            Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFF1F5F9))
                            // Row 4: شلوار, پانچہ
                            SlipMeasureRow("۱۰. شلوار", measurement?.shalwar, "۱۱. پانچہ", measurement?.paicha, "", "")
                        }
                    }

                    // Style preferences
                    if (measurement != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "کٹنگ و سلائی سٹائل:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TailorNavy
                        )
                        Text(
                            text = "کالر: ${measurement.collarStyle} | دامن: ${measurement.damanStyle} | بازو: ${measurement.sleeveStyle}\nجیب: ${measurement.pocketStyle} | پٹی: ${measurement.pattiStyle}",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                        if (measurement.notes.isNotBlank()) {
                            Text(
                                text = "نوٹ: ${measurement.notes}",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Bill Summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("کل بل (Total):", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Rs. ${order.totalAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("پیشگی رقم (Advance):", fontSize = 13.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                        Text("Rs. ${order.advanceAmount.toInt()}", fontSize = 13.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("بقایا واجب الادا (Balance):", fontSize = 14.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.ExtraBold)
                        Text("Rs. ${order.balanceAmount.toInt()}", fontSize = 15.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.ExtraBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = shopProfile?.slipFooterNote ?: "شکریہ برائے انتخاب!",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action buttons: WhatsApp share & System Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val rawPhone = (customer?.phone ?: order.customerPhone).replace(Regex("[^0-9]"), "")
                            // Format for WhatsApp (replace starting 0 with 92 for Pakistan if applicable)
                            val formattedPhone = when {
                                rawPhone.startsWith("0") -> "92" + rawPhone.substring(1)
                                rawPhone.startsWith("92") -> rawPhone
                                else -> rawPhone
                            }
                            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$formattedPhone&text=${Uri.encode(slipText)}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("whatsapp_share_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("واٹس ایپ پر سلپ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, slipText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "شیئر سلپ")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("system_share_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("شیئر", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SlipMeasureRow(
    label1: String, val1: String?,
    label2: String, val2: String?,
    label3: String, val3: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (label1.isNotBlank()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label1, fontSize = 11.sp, color = Color.Gray)
                Text(val1?.ifBlank { "-" } ?: "-", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TailorNavy)
            }
        }
        if (label2.isNotBlank()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label2, fontSize = 11.sp, color = Color.Gray)
                Text(val2?.ifBlank { "-" } ?: "-", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TailorNavy)
            }
        }
        if (label3.isNotBlank()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label3, fontSize = 11.sp, color = Color.Gray)
                Text(val3?.ifBlank { "-" } ?: "-", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TailorNavy)
            }
        }
    }
}
