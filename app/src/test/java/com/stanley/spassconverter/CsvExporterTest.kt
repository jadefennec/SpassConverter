package com.stanley.spassconverter

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CsvExporterTest {
    @Test
    fun exportAll_omitsBomAndUsesValidGoogleCsvHeader() {
        val csv = CsvExporter.exportAll(
            SPassData(
                passwords = listOf(
                    PasswordEntry(
                        name = "Example",
                        url = "https://example.com",
                        username = "user@example.com",
                        password = "s3cr3t",
                        note = "first line\nsecond line"
                    )
                )
            )
        )

        assertFalse(csv.startsWith("\uFEFF"))
        assertTrue(csv.startsWith("name,url,username,password,note\n"))
        assertTrue(csv.contains("\"first line\nsecond line\""))
    }
}
