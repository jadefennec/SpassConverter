package com.stanley.spassconverter

object CsvExporter {

    fun exportGooglePasswords(data: SPassData): String? {
        if (data.passwords.isEmpty()) return null
        val sb = StringBuilder()
        sb.appendLine("name,url,username,password,note")
        for (p in data.passwords) {
            val resolvedUrl = AppUrlMapper.resolveUrl(p)
            val resolvedName = AppUrlMapper.resolveName(p)

            val note = if (p.isAppPassword) {
                val pkg = extractPackageFromUri(p.url)
                buildString {
                    if (p.note.isNotBlank()) append(p.note).append(" | ")
                    append("App: $pkg")
                }
            } else {
                p.note
            }

            sb.appendLine(csvRow(resolvedName, resolvedUrl, p.username, p.password, note))
        }
        return sb.toString()
    }

    fun exportAll(data: SPassData): String {
        val sb = StringBuilder()

        if (data.passwords.isNotEmpty()) {
            sb.appendLine("name,url,username,password,note")
            for (p in data.passwords) {
                val resolvedUrl = AppUrlMapper.resolveUrl(p)
                val resolvedName = AppUrlMapper.resolveName(p)

                val note = if (p.isAppPassword) {
                    val pkg = extractPackageFromUri(p.url)
                    buildString {
                        if (p.note.isNotBlank()) append(p.note).append(" | ")
                        append("App: $pkg")
                    }
                } else {
                    p.note
                }

                sb.appendLine(csvRow(resolvedName, resolvedUrl, p.username, p.password, note))
            }
        }

        if (data.addresses.isNotEmpty()) {
            if (sb.isNotEmpty()) sb.appendLine()
            sb.appendLine("full_name,company_name,street_address,city,state,zipcode,country_code,phone_number,email")
            for (a in data.addresses) {
                sb.appendLine(csvRow(a.fullName, a.company, a.street, a.city, a.state, a.zipcode, a.country, a.phone, a.email))
            }
        }

        if (data.cards.isNotEmpty()) {
            if (sb.isNotEmpty()) sb.appendLine()
            sb.appendLine("name_on_card,first_six_digits,last_four_digits,expiry_month,expiry_year")
            for (c in data.cards) {
                sb.appendLine(csvRow(c.nameOnCard, c.firstSix, c.lastFour, c.expiryMonth, c.expiryYear))
            }
        }

        if (data.notes.isNotEmpty()) {
            if (sb.isNotEmpty()) sb.appendLine()
            sb.appendLine("title,details")
            for (n in data.notes) {
                sb.appendLine(csvRow(n.title, n.details))
            }
        }

        return sb.toString()
    }

    private fun extractPackageFromUri(androidUri: String): String {
        val atIndex = androidUri.indexOf('@')
        if (atIndex < 0) return androidUri
        return androidUri.substring(atIndex + 1).trimEnd('/')
    }

    private fun csvRow(vararg values: String): String =
        values.joinToString(",") { escapeCsv(it) }

    private fun escapeCsv(value: String): String {
        if (value.contains(',') || value.contains('"') || value.contains('\n')) {
            return "\"${value.replace("\"", "\"\"")}\""
        }
        return value
    }
}
