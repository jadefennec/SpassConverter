# SPASS Converter

> **The fastest way to migrate from Samsung Pass to Google Password Manager** — decrypt your `.spass` export on-device, get a ready-to-import CSV in seconds. Your passwords never leave your phone.

[![License: MIT](https://img.shields.io/github/license/stanley-projects/SpassConverter?color=blue)](LICENSE)
[![Android 8.0+](https://img.shields.io/badge/Android-8.0%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Release](https://img.shields.io/github/v/release/stanley-projects/SpassConverter?color=teal)](https://github.com/stanley-projects/SpassConverter/releases)
[![No Internet](https://img.shields.io/badge/Internet-none-critical?logo=shield&logoColor=white)](app/src/main/AndroidManifest.xml)

Samsung Pass lets you export your saved passwords as an encrypted `.spass` file — but no standard password manager can open it directly. SPASS Converter decrypts the file on your phone using your Samsung Pass password, then produces a CSV you can import straight into **Google Password Manager / Chrome**, **Bitwarden**, **1Password**, **KeePass**, or any tool that accepts the standard CSV format.

**Nothing ever leaves your device.** Every network permission is explicitly blocked at the manifest level.

---

## Features

- 🔒 **100% offline** — INTERNET, ACCESS\_NETWORK\_STATE, BLUETOOTH, NFC all explicitly removed in `AndroidManifest.xml`
- 🔑 Decrypts Samsung Pass `.spass` export files (AES-256-CBC, PBKDF2-SHA256, 70 000 iterations)
- 📋 Exports **passwords, credit cards, addresses, and notes** to separate CSV sections
- ✅ Password CSV column order matches the **Google Password Manager** import format exactly
- 🌐 Resolves Android app package names to real website URLs (100+ known apps)
- 📊 Plain UTF-8 CSV output stays compatible with Google Password Manager and spreadsheet apps
- 🧹 Sensitive data (password, decrypted CSV) zeroed from memory on app leave
- 🎨 Material 3 UI, dark mode, tablet / foldable support — Android 8.0 (API 26)+

---

## What gets exported

| Section | CSV columns |
|---------|-------------|
| Passwords | `name, url, username, password, note` |
| Addresses | `full_name, company_name, street_address, city, state, zipcode, country_code, phone_number, email` |
| Credit cards | `name_on_card, first_six_digits, last_four_digits, expiry_month, expiry_year` |
| Notes | `title, details` |

The password section is **directly importable** into Google Password Manager, Chrome, Bitwarden, and 1Password CSV import.

---

## How to use

1. **Export from Samsung Pass** — open Samsung Pass → ⚙️ Settings → *Export passwords* → save the `.spass` file to device storage
2. **Open SPASS Converter** → tap the file picker → select your `.spass` file
3. **Enter your Samsung Pass password** (the one you use to unlock Samsung Pass itself)
4. Tap **Convert** — you'll see a summary: *12 passwords, 2 credit cards, 1 address*
5. Tap the preview icon to verify the CSV looks correct
6. Tap **Save CSV** → choose a location (Downloads, Drive, etc.)
7. **Import into your new password manager:**
   - **Google Password Manager / Chrome:** go to [passwords.google.com](https://passwords.google.com) → ⚙️ Settings → Import passwords → select your CSV
   - **Bitwarden:** Tools → Import data → Google Chrome (CSV) format
   - **1Password:** File → Import → Chrome

---

## FAQ

### How do I import Samsung Pass passwords into Google Password Manager?
Export from Samsung Pass (Settings → Export passwords) → convert with this app → import the CSV at [passwords.google.com](https://passwords.google.com) → Settings → Import passwords. The CSV column format matches exactly.

### How do I open a .spass file?
A `.spass` file is an AES-256 encrypted export from Samsung Pass. You cannot open it with a text editor or spreadsheet. Use SPASS Converter to decrypt it on your device and convert it to a readable CSV.

### Does this app upload my passwords anywhere?
No. The app has no internet connection at all — every network permission is explicitly removed in the manifest (`tools:node="remove"`). Decryption and conversion happen entirely in memory on your device.

### What's the Samsung Pass export password?
It's the password you use to unlock Samsung Pass itself — not your Samsung account password. You set it when you first configured Samsung Pass.

### Can I import into Bitwarden instead of Google?
Yes. In Bitwarden's web vault go to Tools → Import data → select **Google Chrome (CSV)** as the format, then upload the CSV. The column layout is compatible.

### Can I import into 1Password?
Yes. Use 1Password's built-in CSV import (File → Import → 1Password or Generic CSV). You may need to map columns during import.

### Does it work with Samsung Pass app passwords (android://)?
Yes. App passwords stored in Samsung Pass use `android://` URIs. SPASS Converter includes a resolver for 100+ common Android package names (Instagram, WhatsApp, Netflix, Spotify, Cash App, etc.) that maps them to real website URLs automatically.

### Is this open source?
Yes — MIT License. Source code is at [github.com/stanley-projects/SpassConverter](https://github.com/stanley-projects/SpassConverter).

---

## Building from source

```bash
git clone https://github.com/stanley-projects/SpassConverter.git
cd SpassConverter
./gradlew assembleRelease   # unsigned APK — no local.properties needed
```

The same build runs automatically for pushes to `master` and pull requests. To
start one manually, open the repository's **Actions** tab, select **Build
Android app**, and choose **Run workflow**. Each successful run publishes the
release APK as the `spass-converter-release` artifact.

For a local signed release, add to `local.properties` (this file is gitignored):
```
RELEASE_STORE_FILE=/path/to/your.keystore
RELEASE_STORE_PASSWORD=yourpassword
RELEASE_KEY_ALIAS=youralias
RELEASE_KEY_PASSWORD=yourkeypassword
```

The GitHub Actions workflow signs release builds on pushes to `master` and
manual runs when all four repository secrets are configured:

- `RELEASE_KEYSTORE_BASE64` - the PKCS#12 keystore encoded with `base64`
- `RELEASE_STORE_PASSWORD`
- `RELEASE_KEY_ALIAS`
- `RELEASE_KEY_PASSWORD`

The workflow validates the keystore before Gradle runs and verifies the
resulting APK with `apksigner`. Pull requests from forks continue to produce
unsigned validation APKs because GitHub does not expose repository secrets to
them. Signed runs publish the `spass-converter-signed-release` artifact;
pull-request validation runs publish `spass-converter-unsigned-validation`.

---

## License

MIT — see [LICENSE](LICENSE).

---

<sub>Keywords: samsung pass export · spass to csv · convert samsung pass to google password manager · import samsung pass to chrome · open spass file · samsung pass migration · samsung pass android · password export android · spass file format · samsung to bitwarden · samsung to 1password</sub>
