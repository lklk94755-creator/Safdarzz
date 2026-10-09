package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Measurement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Ezy Tailor Master", appName)
  }

  @Test
  fun `verify eleven custom tailor measurements order and defaults`() {
    val measurement = Measurement(
      customerId = 1L,
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
      paicha = "8.0"
    )

    assertEquals("40.5", measurement.lambai)     // 1. لمبائی
    assertEquals("18.5", measurement.teera)      // 2. تیرہ
    assertEquals("24.0", measurement.bazu)       // 3. بازو
    assertEquals("15.5", measurement.collar)     // 4. کالر
    assertEquals("38.0", measurement.chhati)     // 5. چھاتی
    assertEquals("36.0", measurement.kamar)      // 6. کمر
    assertEquals("19.5", measurement.halfChhati) // 7. ہاف چھاتی
    assertEquals("5.5", measurement.jeb)         // 8. جیب
    assertEquals("23.5", measurement.daman)      // 9. دامن
    assertEquals("39.0", measurement.shalwar)    // 10. شلوار
    assertEquals("8.0", measurement.paicha)      // 11. پانچہ
  }
}
