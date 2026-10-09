package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Customer
import com.example.data.model.Measurement
import com.example.ui.components.MeasurementInputCard
import com.example.ui.theme.TailorGold
import com.example.ui.theme.TailorNavy

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MeasurementEditScreen(
    customer: Customer,
    initialMeasurement: Measurement?,
    onSave: (Measurement) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    // State for all 11 measurements in order
    var lambai by remember { mutableStateOf(initialMeasurement?.lambai ?: "") }
    var teera by remember { mutableStateOf(initialMeasurement?.teera ?: "") }
    var bazu by remember { mutableStateOf(initialMeasurement?.bazu ?: "") }
    var collar by remember { mutableStateOf(initialMeasurement?.collar ?: "") }
    var chhati by remember { mutableStateOf(initialMeasurement?.chhati ?: "") }
    var kamar by remember { mutableStateOf(initialMeasurement?.kamar ?: "") }
    var halfChhati by remember { mutableStateOf(initialMeasurement?.halfChhati ?: "") }
    var jeb by remember { mutableStateOf(initialMeasurement?.jeb ?: "") }
    var daman by remember { mutableStateOf(initialMeasurement?.daman ?: "") }
    var shalwar by remember { mutableStateOf(initialMeasurement?.shalwar ?: "") }
    var paicha by remember { mutableStateOf(initialMeasurement?.paicha ?: "") }

    // State for styles
    var collarStyle by remember { mutableStateOf(initialMeasurement?.collarStyle ?: "بین (Bain)") }
    var damanStyle by remember { mutableStateOf(initialMeasurement?.damanStyle ?: "گول دامن (Round)") }
    var sleeveStyle by remember { mutableStateOf(initialMeasurement?.sleeveStyle ?: "سادہ بازو (Open)") }
    var pocketStyle by remember { mutableStateOf(initialMeasurement?.pocketStyle ?: "سامنے ۱، سائیڈ ۲") }
    var pattiStyle by remember { mutableStateOf(initialMeasurement?.pattiStyle ?: "بٹن پٹی (Exposed)") }
    var notes by remember { mutableStateOf(initialMeasurement?.notes ?: "") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "پیمائش بک (11 Measurement Options)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = customer.name,
                            fontSize = 12.sp,
                            color = TailorGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val updated = (initialMeasurement ?: Measurement(customerId = customer.id)).copy(
                                customerId = customer.id,
                                lambai = lambai,
                                teera = teera,
                                bazu = bazu,
                                collar = collar,
                                chhati = chhati,
                                kamar = kamar,
                                halfChhati = halfChhati,
                                jeb = jeb,
                                daman = daman,
                                shalwar = shalwar,
                                paicha = paicha,
                                collarStyle = collarStyle,
                                damanStyle = damanStyle,
                                sleeveStyle = sleeveStyle,
                                pocketStyle = pocketStyle,
                                pattiStyle = pattiStyle,
                                notes = notes,
                                updatedAt = System.currentTimeMillis()
                            )
                            onSave(updated)
                        },
                        modifier = Modifier.testTag("save_measurement_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = TailorGold
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            val updated = (initialMeasurement ?: Measurement(customerId = customer.id)).copy(
                                customerId = customer.id,
                                lambai = lambai,
                                teera = teera,
                                bazu = bazu,
                                collar = collar,
                                chhati = chhati,
                                kamar = kamar,
                                halfChhati = halfChhati,
                                jeb = jeb,
                                daman = daman,
                                shalwar = shalwar,
                                paicha = paicha,
                                collarStyle = collarStyle,
                                damanStyle = damanStyle,
                                sleeveStyle = sleeveStyle,
                                pocketStyle = pocketStyle,
                                pattiStyle = pattiStyle,
                                notes = notes,
                                updatedAt = System.currentTimeMillis()
                            )
                            onSave(updated)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("save_measurement_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "پیمائش محفوظ کریں (Save 11 Measurements)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Customer Header Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = customer.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "فون: ${customer.phone} | کد: #${customer.customerCode.ifBlank { customer.id.toString() }}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = TailorGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "درزی پیمائش کے ۱۱ نکات",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "سائز انچ میں (Inches)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // --- THE 11 MEASUREMENT CARDS IN PRECISE ORDER ---

            // ۱. لمبائی (Length)
            MeasurementInputCard(
                numberLabelUrdu = "۱. لمبائی",
                englishLabel = "Kameez Length (Lambai)",
                value = lambai,
                onValueChange = { lambai = it }
            )

            // ۲۔ تیرہ (Teera / Shoulder)
            MeasurementInputCard(
                numberLabelUrdu = "۲۔ تیرہ",
                englishLabel = "Shoulder (Teera)",
                value = teera,
                onValueChange = { teera = it }
            )

            // ۳. بازو (Bazu / Sleeves)
            MeasurementInputCard(
                numberLabelUrdu = "۳. بازو",
                englishLabel = "Sleeve (Bazu)",
                value = bazu,
                onValueChange = { bazu = it }
            )

            // ۴. کالر (Collar / Bain)
            MeasurementInputCard(
                numberLabelUrdu = "۴. کالر",
                englishLabel = "Collar / Bain",
                value = collar,
                onValueChange = { collar = it }
            )

            // ۵. چھاتی (Chhati / Chest)
            MeasurementInputCard(
                numberLabelUrdu = "۵. چھاتی",
                englishLabel = "Chest (Chhati)",
                value = chhati,
                onValueChange = { chhati = it }
            )

            // ۶. کمر (Kamar / Waist)
            MeasurementInputCard(
                numberLabelUrdu = "۶. کمر",
                englishLabel = "Waist (Kamar)",
                value = kamar,
                onValueChange = { kamar = it }
            )

            // ۷. ہاف چھاتی (Half Chhati)
            MeasurementInputCard(
                numberLabelUrdu = "۷. ہاف چھاتی",
                englishLabel = "Half Chest (Front)",
                value = halfChhati,
                onValueChange = { halfChhati = it }
            )

            // ۸. جیب (Jeb / Pocket)
            MeasurementInputCard(
                numberLabelUrdu = "۸. جیب",
                englishLabel = "Pocket Length (Jeb)",
                value = jeb,
                onValueChange = { jeb = it }
            )

            // ۹. دامن (Daman / Ghera)
            MeasurementInputCard(
                numberLabelUrdu = "۹. دامن",
                englishLabel = "Bottom Ghera (Daman)",
                value = daman,
                onValueChange = { daman = it }
            )

            // ۱۰. شلوار (Shalwar / Trouser Length)
            MeasurementInputCard(
                numberLabelUrdu = "۱۰. شلوار",
                englishLabel = "Trouser Length (Shalwar)",
                value = shalwar,
                onValueChange = { shalwar = it }
            )

            // ۱۱. پانچہ (Paicha / Bottom Opening)
            MeasurementInputCard(
                numberLabelUrdu = "۱۱. پانچہ",
                englishLabel = "Bottom Cuff (Paicha)",
                value = paicha,
                onValueChange = { paicha = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- STITCHING & CUTTING STYLES ---
            Text(
                text = "✂️ سلائی اور کٹائی کا انتخاب (Styling Options)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Collar Style
            StyleSelectorSection(
                title = "کالر کا انداز (Collar Style):",
                options = listOf("بین (Bain)", "کالر (Standard)", "ہاف بین (Half Bain)", "اوپن گلا (Open Neck)"),
                selected = collarStyle,
                onSelect = { collarStyle = it }
            )

            // 2. Daman Style
            StyleSelectorSection(
                title = "دامن کا انداز (Daman Style):",
                options = listOf("گول دامن (Round)", "چورس دامن (Square)"),
                selected = damanStyle,
                onSelect = { damanStyle = it }
            )

            // 3. Sleeve Style
            StyleSelectorSection(
                title = "بازو و کف (Sleeve & Cuff):",
                options = listOf("سادہ بازو (Open)", "کف (Cuff)", "گول کف (Round Cuff)", "کٹ کف (Cut Cuff)"),
                selected = sleeveStyle,
                onSelect = { sleeveStyle = it }
            )

            // 4. Pocket Style
            StyleSelectorSection(
                title = "جیبیں (Pockets):",
                options = listOf("سامنے ۱، سائیڈ ۲", "سامنے ۱، سائیڈ ۱", "صرف سائیڈ ۲", "شلوار جیب کے ساتھ"),
                selected = pocketStyle,
                onSelect = { pocketStyle = it }
            )

            // 5. Patti Style
            StyleSelectorSection(
                title = "سامنے پٹی (Front Placket):",
                options = listOf("بٹن پٹی (Exposed)", "گم پٹی (Concealed)", "زپ (Zip)", "ڈبل سلائی پٹی"),
                selected = pattiStyle,
                onSelect = { pattiStyle = it }
            )

            // Extra notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("measurement_notes_field"),
                label = { Text("اضافی ہدایات / کلف وغیرہ (Extra Notes)") },
                placeholder = { Text("مثال: ہارڈ کلف، کپڑے میں کھنچاؤ، وغیرہ") },
                minLines = 2,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StyleSelectorSection(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { opt ->
                FilterChip(
                    selected = selected == opt,
                    onClick = { onSelect(opt) },
                    label = { Text(opt, fontSize = 12.sp) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}
