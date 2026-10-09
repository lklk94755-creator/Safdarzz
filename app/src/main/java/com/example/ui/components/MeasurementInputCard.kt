package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TailorGold
import com.example.ui.theme.TailorGoldLight
import com.example.ui.theme.TailorNavy

@Composable
fun MeasurementInputCard(
    numberLabelUrdu: String,    // e.g. "۱. لمبائی"
    englishLabel: String,       // e.g. "Length"
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    unit: String = "انچ (in)"
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("measurement_card_$englishLabel"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: Urdu Title and English subtitle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(TailorGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = numberLabelUrdu,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = englishLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Input Row with +/- quick adjust buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus button
                IconButton(
                    onClick = {
                        val current = value.toDoubleOrNull() ?: 0.0
                        if (current >= 0.5) {
                            val newVal = current - 0.5
                            onValueChange(if (newVal % 1.0 == 0.0) newVal.toInt().toString() else newVal.toString())
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("minus_$englishLabel")
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Minus 0.5",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Text field
                OutlinedTextField(
                    value = value,
                    onValueChange = { input ->
                        // Allow digits and decimal point
                        if (input.isEmpty() || input.matches(Regex("""^\d*\.?\d*$"""))) {
                            onValueChange(input)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_$englishLabel"),
                    placeholder = { Text("0.0", color = MaterialTheme.colorScheme.outline) },
                    suffix = { Text(unit, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TailorGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Plus button
                IconButton(
                    onClick = {
                        val current = value.toDoubleOrNull() ?: 0.0
                        val newVal = current + 0.5
                        onValueChange(if (newVal % 1.0 == 0.0) newVal.toInt().toString() else newVal.toString())
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("plus_$englishLabel")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Plus 0.5",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Tailor quick fraction chips (Sawa / Sadhe / Paune / Pura)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TailorFractionChip(label = ".0 (پورا)", fraction = ".0", currentValue = value, onApply = { onValueChange(it) })
                TailorFractionChip(label = ".25 (سوا)", fraction = ".25", currentValue = value, onApply = { onValueChange(it) })
                TailorFractionChip(label = ".5 (ساڑھے)", fraction = ".5", currentValue = value, onApply = { onValueChange(it) })
                TailorFractionChip(label = ".75 (پونے)", fraction = ".75", currentValue = value, onApply = { onValueChange(it) })
            }
        }
    }
}

@Composable
private fun TailorFractionChip(
    label: String,
    fraction: String,
    currentValue: String,
    onApply: (String) -> Unit
) {
    val integerPart = currentValue.substringBefore(".")
    val safeInt = if (integerPart.isBlank()) "0" else integerPart

    SuggestionChip(
        onClick = {
            if (fraction == ".0") {
                onApply(safeInt)
            } else {
                onApply("$safeInt$fraction")
            }
        },
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        },
        shape = RoundedCornerShape(6.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = null,
        modifier = Modifier.height(28.dp)
    )
}
