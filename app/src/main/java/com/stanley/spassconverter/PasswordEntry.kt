package com.stanley.spassconverter

data class PasswordEntry(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val note: String,
    val title: String = "",
    val appName: String = "",
    val packageName: String = ""
) {
    val isAppPassword: Boolean get() = url.startsWith("android://")
}

data class AddressEntry(
    val fullName: String,
    val company: String,
    val street: String,
    val city: String,
    val state: String,
    val zipcode: String,
    val country: String,
    val phone: String,
    val email: String
)

data class CardEntry(
    val nameOnCard: String,
    val firstSix: String,
    val lastFour: String,
    val expiryMonth: String,
    val expiryYear: String
)

data class NoteEntry(
    val title: String,
    val details: String
)

data class SPassData(
    val passwords: List<PasswordEntry> = emptyList(),
    val addresses: List<AddressEntry> = emptyList(),
    val cards: List<CardEntry> = emptyList(),
    val notes: List<NoteEntry> = emptyList()
) {
    val totalEntries get() = passwords.size + addresses.size + cards.size + notes.size
    val isEmpty get() = totalEntries == 0

    fun summary(): String = buildList {
        if (passwords.isNotEmpty()) add("${passwords.size} password(s)")
        if (addresses.isNotEmpty()) add("${addresses.size} address(es)")
        if (cards.isNotEmpty()) add("${cards.size} card(s)")
        if (notes.isNotEmpty()) add("${notes.size} note(s)")
    }.joinToString(", ")
}
