package com.stanley.spassconverter

class SPassParser {

    class ParseException(message: String) : Exception(message)

    fun parse(decryptedText: String): SPassData {
        val sections = decryptedText.split("next_table")

        if (sections.size < 2) {
            throw ParseException("Unrecognized file format")
        }

        val headerLines = sections[0].trim().lines()
        val modules = if (headerLines.size >= 2) {
            headerLines[1].split(";").map { it.trim().lowercase() == "true" }
        } else {
            emptyList()
        }

        val passwords = if (modules.getOrElse(0) { false } && sections.size > 1)
            parsePasswords(sections[1]) else emptyList()

        val cards = if (modules.getOrElse(1) { false } && sections.size > 2)
            parseCards(sections[2]) else emptyList()

        val addresses = if (modules.getOrElse(2) { false } && sections.size > 3)
            parseAddresses(sections[3]) else emptyList()

        val notes = if (modules.getOrElse(3) { false } && sections.size > 4)
            parseNotes(sections[4]) else emptyList()

        return SPassData(passwords, addresses, cards, notes)
    }

    private fun parsePasswords(section: String): List<PasswordEntry> {
        val (header, rows) = splitSection(section) ?: return emptyList()
        val m = headerMap(header)

        val urlIdx = m["origin_url"] ?: 1
        val usernameIdx = m["username_value"] ?: 4
        val passwordIdx = m["password_value"] ?: 7
        val noteIdx = m["credential_memo"] ?: 31
        val titleIdx = m["title"] ?: 17
        val appNameIdx = m["app_name"] ?: 20
        val packageNameIdx = m["package_name"] ?: 21

        return rows.mapNotNull { row ->
            val f = row.split(";")
            try {
                val url = decode(f, urlIdx)
                val title = decode(f, titleIdx)
                val appName = decode(f, appNameIdx)
                val packageName = decode(f, packageNameIdx)

                PasswordEntry(
                    name = url,
                    url = url,
                    username = decode(f, usernameIdx),
                    password = decode(f, passwordIdx),
                    note = decode(f, noteIdx),
                    title = title,
                    appName = appName,
                    packageName = packageName
                )
            } catch (_: Exception) { null }
        }
    }

    private fun parseAddresses(section: String): List<AddressEntry> {
        val (header, rows) = splitSection(section) ?: return emptyList()
        val m = headerMap(header)

        return rows.mapNotNull { row ->
            val f = row.split(";")
            try {
                AddressEntry(
                    fullName = decode(f, m["full_name"] ?: 1),
                    company = decode(f, m["company_name"] ?: 2),
                    street = decode(f, m["street_address"] ?: 3),
                    city = decode(f, m["city"] ?: 4),
                    state = decode(f, m["state"] ?: 5),
                    zipcode = decode(f, m["zipcode"] ?: 6),
                    country = decode(f, m["country_code"] ?: 7),
                    phone = decode(f, m["phone_number"] ?: 8),
                    email = decode(f, m["email"] ?: 9)
                )
            } catch (_: Exception) { null }
        }
    }

    private fun parseCards(section: String): List<CardEntry> {
        val (header, rows) = splitSection(section) ?: return emptyList()
        val m = headerMap(header)

        return rows.mapNotNull { row ->
            val f = row.split(";")
            try {
                CardEntry(
                    nameOnCard = decode(f, m["name_on_card"] ?: 4),
                    firstSix = decode(f, m["first_six_digit"] ?: 2),
                    lastFour = decode(f, m["last_four_digit"] ?: 3),
                    expiryMonth = decode(f, m["expiration_month"] ?: 5),
                    expiryYear = decode(f, m["expiration_year"] ?: 6)
                )
            } catch (_: Exception) { null }
        }
    }

    private fun parseNotes(section: String): List<NoteEntry> {
        val (header, rows) = splitSection(section) ?: return emptyList()
        val m = headerMap(header)

        return rows.mapNotNull { row ->
            val f = row.split(";")
            try {
                NoteEntry(
                    title = decode(f, m["note_title"] ?: 1),
                    details = decode(f, m["note_details"] ?: 2)
                )
            } catch (_: Exception) { null }
        }
    }

    private fun splitSection(section: String): Pair<List<String>, List<String>>? {
        val rows = section.trim().lines().filter { it.isNotBlank() }
        if (rows.size < 2) return null
        return rows[0].split(";") to rows.drop(1)
    }

    private fun headerMap(header: List<String>): Map<String, Int> =
        header.withIndex().associate { (idx, name) -> name.trim().lowercase() to idx }

    private fun decode(fields: List<String>, index: Int): String {
        if (index < 0 || index >= fields.size) return ""
        val value = fields[index].trim()
        if (value.isBlank()) return ""
        return try {
            String(java.util.Base64.getDecoder().decode(value), Charsets.UTF_8)
        } catch (_: IllegalArgumentException) {
            value
        }
    }
}
