# Phase 5 — Play Console Fill-In Guide

Exact values for each field. File paths use the project folder `SpassConverter`.

---

## CREATE APP (first-time setup)

| Field | Value |
|-------|-------|
| **App name** | SPASS Converter |
| **Default language** | English (United States) — or your choice |
| **App or Game** | App |
| **Free or Paid** | Free |
| **Developer Program Policies** | Check to accept |
| **US export laws** | Check to accept |

---

## MAIN STORE LISTING

### Short description
Max 80 characters. Paste exactly:
```
Convert Samsung Pass exports to CSV. Fully offline—nothing leaves your device.
```

### Full description
Max 4000 characters. Copy from file:
**File:** `store-assets\full-description.txt`

Or paste this:
```
SPASS Converter lets you convert Samsung Pass export files (.spass) to CSV format directly on your Android device. No account, no sign‑in, no internet—everything runs locally.

WHAT IT DOES
• Import your Samsung Pass export file (.spass)
• Enter the password you set when exporting from Samsung Pass
• Convert to standard CSV that works with Bitwarden, 1Password, KeePass, and other password managers
• Preview and share the CSV to your preferred app or cloud backup

FULLY OFFLINE
The app never connects to the internet. All processing happens on your device. Your Samsung Pass export and password are never transmitted anywhere. No data is collected, stored, or shared with third parties.

HOW TO EXPORT FROM SAMSUNG PASS
1. Open Samsung Pass on your device
2. Go to Settings → Export or Back up data
3. Choose to export your passwords and follow the prompts
4. Save the .spass file and remember the password you set
5. Open SPASS Converter, select the file, enter the password, and get your CSV

COMPATIBILITY
The CSV output is compatible with popular password managers such as Bitwarden, 1Password, KeePass, LastPass, and similar tools that accept CSV imports.

PRIVACY
Your data stays on your device. We do not collect or transmit anything. See our Privacy Policy for full details.
```

### App icon (512×512 PNG, max 1MB)
**File:** `store-assets\app-icon-512.png`

**Note:** If the file is over 1MB, compress it at [TinyPNG.com](https://tinypng.com) first.

### Feature graphic (1024×500 PNG or JPG)
**File:** `store-assets\feature-graphic-1024x500.png`

### Screenshots (minimum 2)
**Upload screen** — Your screenshot of the main upload/tap-to-select screen  
**Success screen** — Your screenshot of the conversion complete screen

**Where to get them:** The two screenshots you took earlier. Save them to your PC and upload. They should be in your phone’s gallery or wherever you transferred them.

---

## CONTENT RATING

**Start questionnaire** → Answer each question:

| Question | Answer |
|----------|--------|
| **Category** | Utility |
| **Violence, blood, gore** | No |
| **Sexual content** | No |
| **Nudity** | No |
| **Language** | No (or appropriate for everyone) |
| **Drugs, alcohol, tobacco** | No |
| **Gambling** | No |
| **Horror / fear themes** | No |
| **User-generated content** | No |
| **Shares user location** | No |
| **Shares personal info** | No — data is processed locally, never transmitted |

**Sensitive data / passwords:**  
State that the app processes passwords locally on-device only; no data is transmitted off the device.

---

## DATA SAFETY

| Question | Answer |
|----------|--------|
| **Does your app collect or share any user data?** | No |
| **Does your app use encryption in transit?** | No (app is fully offline; no data is ever transmitted) |
| **Can users request that their data be deleted?** | Yes (no data is stored, so N/A) |

---

## ADS

| Question | Answer |
|----------|--------|
| **Does your app contain ads?** | No, my app does not contain ads |

---

## TARGET AUDIENCE

| Question | Answer |
|----------|--------|
| **Target age group** | 18 and over (recommended for password utility) |
| **Primarily appeals to children?** | No |

---

## PRIVACY POLICY

**URL to paste:**
```
https://stanley-projects.github.io/SpassConverter/
```

---

## APP ACCESS

| Question | Answer |
|----------|--------|
| **Is all or most functionality accessible without special access?** | Yes — All or most functionality is accessible without special access |

**Special instructions (if asked):** The app opens directly to the upload screen. No login required. Users simply tap to select a file and enter their export password.

---

## FILE PATHS (for uploads)

| Asset | Full path |
|-------|-----------|
| App icon | `c:\Users\HP\Coding Projects\Android Studio Projects\SpassConverter\store-assets\app-icon-512.png` |
| Feature graphic | `c:\Users\HP\Coding Projects\Android Studio Projects\SpassConverter\store-assets\feature-graphic-1024x500.png` |
