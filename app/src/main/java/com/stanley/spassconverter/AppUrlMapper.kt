package com.stanley.spassconverter

object AppUrlMapper {

    fun resolveUrl(entry: PasswordEntry): String {
        if (!entry.isAppPassword) return entry.url

        val pkg = extractPackageName(entry.url).ifEmpty { entry.packageName }
        if (pkg.isBlank()) return entry.url

        KNOWN_APPS[pkg]?.let { return it }

        return deriveUrlFromPackage(pkg)
    }

    fun resolveName(entry: PasswordEntry): String {
        if (!entry.isAppPassword) return entry.url

        if (entry.appName.isNotBlank()) return entry.appName
        if (entry.title.isNotBlank()) return entry.title

        val pkg = extractPackageName(entry.url).ifEmpty { entry.packageName }
        if (pkg.isBlank()) return entry.url

        KNOWN_APP_NAMES[pkg]?.let { return it }

        return humanizePackageName(pkg)
    }

    private fun extractPackageName(androidUri: String): String {
        val atIndex = androidUri.indexOf('@')
        if (atIndex < 0) return ""
        return androidUri.substring(atIndex + 1).trimEnd('/')
    }

    private fun deriveUrlFromPackage(pkg: String): String {
        val parts = pkg.split(".")
        if (parts.size < 2) return "https://$pkg"

        val reversed = parts.reversed()
        val domain = when {
            reversed.size >= 2 -> "${reversed[0]}.${reversed[1]}"
            else -> reversed[0]
        }

        if (domain.isBlank()) return "https://$pkg"

        val tld = parts[0]
        val company = parts[1]
        return "https://www.$company.$tld"
    }

    private fun humanizePackageName(pkg: String): String {
        val parts = pkg.split(".")
        val meaningful = parts.drop(1).filter {
            it !in setOf("android", "app", "mobile", "lite", "pro", "frontpage", "nextgen")
        }
        return (meaningful.firstOrNull() ?: parts.lastOrNull() ?: pkg)
            .replaceFirstChar { it.uppercase() }
    }

    private val KNOWN_APPS = mapOf(
        // Social
        "com.instagram.android" to "https://www.instagram.com",
        "com.instagram.lite" to "https://www.instagram.com",
        "com.facebook.katana" to "https://www.facebook.com",
        "com.facebook.lite" to "https://www.facebook.com",
        "com.facebook.orca" to "https://www.messenger.com",
        "com.twitter.android" to "https://x.com",
        "com.twitter.android.lite" to "https://x.com",
        "com.zhiliaoapp.musically" to "https://www.tiktok.com",
        "com.snapchat.android" to "https://www.snapchat.com",
        "com.reddit.frontpage" to "https://www.reddit.com",
        "com.linkedin.android" to "https://www.linkedin.com",
        "com.pinterest" to "https://www.pinterest.com",
        "com.tumblr" to "https://www.tumblr.com",
        "com.discord" to "https://discord.com",

        // Messaging
        "com.whatsapp" to "https://web.whatsapp.com",
        "com.whatsapp.w4b" to "https://web.whatsapp.com",
        "org.telegram.messenger" to "https://web.telegram.org",
        "org.thoughtcrime.securesms" to "https://signal.org",
        "com.viber.voip" to "https://www.viber.com",

        // Streaming
        "com.google.android.youtube" to "https://www.youtube.com",
        "com.spotify.music" to "https://www.spotify.com",
        "com.netflix.mediaclient" to "https://www.netflix.com",
        "com.amazon.avod.thirdpartyclient" to "https://www.primevideo.com",
        "com.disney.disneyplus" to "https://www.disneyplus.com",
        "com.hbo.hbonow" to "https://www.max.com",
        "tv.twitch.android.app" to "https://www.twitch.tv",

        // Shopping
        "com.amazon.mShop.android.shopping" to "https://www.amazon.com",
        "com.rfi.sams.android" to "https://www.samsclub.com",
        "com.ebay.mobile" to "https://www.ebay.com",
        "com.alibaba.aliexpresshd" to "https://www.aliexpress.com",
        "com.shopee.id" to "https://shopee.com",
        "com.walmart.android" to "https://www.walmart.com",

        // Finance
        "com.paypal.android.p2pmobile" to "https://www.paypal.com",
        "com.venmo" to "https://venmo.com",
        "com.squareup.cash" to "https://cash.app",
        "com.coinbase.android" to "https://www.coinbase.com",
        "com.binance.dev" to "https://www.binance.com",
        "com.bybit.app" to "https://www.bybit.com",
        "com.robinhood.android" to "https://robinhood.com",
        "com.kudabank.app" to "https://www.kuda.com",
        "com.piggybankng.piggy" to "https://www.piggyvest.com",
        "com.accessbank.nextgen" to "https://www.accessbankplc.com",

        // Productivity
        "com.microsoft.office.outlook" to "https://outlook.live.com",
        "com.google.android.gm" to "https://mail.google.com",
        "com.slack" to "https://slack.com",
        "com.Slack" to "https://slack.com",
        "us.zoom.videomeetings" to "https://zoom.us",
        "com.microsoft.teams" to "https://teams.microsoft.com",
        "com.notion.id" to "https://www.notion.so",
        "com.trello" to "https://trello.com",
        "com.dropbox.android" to "https://www.dropbox.com",
        "com.google.android.apps.docs" to "https://drive.google.com",

        // Ride / Delivery
        "com.ubercab" to "https://www.uber.com",
        "com.ubercab.eats" to "https://www.ubereats.com",
        "me.lyft.android" to "https://www.lyft.com",
        "com.dd.doordash" to "https://www.doordash.com",

        // Travel
        "com.booking" to "https://www.booking.com",
        "com.airbnb.android" to "https://www.airbnb.com",

        // Gaming
        "com.epicgames.fortnite" to "https://www.epicgames.com",
        "com.roblox.client" to "https://www.roblox.com",
        "com.supercell.clashofclans" to "https://supercell.com",

        // Dev / Tech
        "com.github.android" to "https://github.com",
        "io.github.nicehash" to "https://www.nicehash.com",

        // Other from user's data
        "com.tickpickllc.ceobrien.tickpick" to "https://www.tickpick.com",
        "co.grey.mobile.android" to "https://grey.co",
        "com.divest.app" to "https://www.getdivest.com",
        "com.macrovideo.v380pro" to "https://www.v380.com",
        "com.jozy.whipcareapp2" to "https://whipcare.com",
        "com.google.android.apps.authenticator2" to "https://myaccount.google.com",
    )

    private val KNOWN_APP_NAMES = mapOf(
        "com.zhiliaoapp.musically" to "TikTok",
        "com.facebook.katana" to "Facebook",
        "com.facebook.lite" to "Facebook Lite",
        "com.facebook.orca" to "Messenger",
        "com.twitter.android" to "X (Twitter)",
        "com.google.android.apps.authenticator2" to "Google Authenticator",
        "com.amazon.avod.thirdpartyclient" to "Prime Video",
        "com.amazon.mShop.android.shopping" to "Amazon",
        "com.rfi.sams.android" to "Sam's Club",
        "org.thoughtcrime.securesms" to "Signal",
        "com.squareup.cash" to "Cash App",
        "com.ubercab.eats" to "Uber Eats",
        "com.dd.doordash" to "DoorDash",
        "tv.twitch.android.app" to "Twitch",
        "co.grey.mobile.android" to "Grey",
        "com.accessbank.nextgen" to "Access Bank",
        "com.piggybankng.piggy" to "PiggyVest",
        "com.macrovideo.v380pro" to "V380 Pro",
        "com.jozy.whipcareapp2" to "WhipCare",
        "com.reddit.frontpage" to "Reddit",
        "com.kudabank.app" to "Kuda Bank",
    )
}
