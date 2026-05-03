# SPASS Converter

Android app that converts Samsung Pass (`.spass`) encrypted export files into a standard CSV format compatible with password managers like Bitwarden, 1Password, and others.

- Package: `com.stanley.spassconverter`
- Min SDK: 26 (Android 8.0)
- Target SDK: 36
- Fully offline — no internet permissions, no tracking
- Jetpack Compose UI

## Build

```bash
./gradlew assembleRelease
```

Release signing requires `local.properties` to define:
```
RELEASE_STORE_FILE=<path to keystore>
RELEASE_STORE_PASSWORD=<password>
RELEASE_KEY_PASSWORD=<password>
```

Without those properties the build compiles and produces an unsigned release APK (suitable for F-Droid or CI).

## Distribution

- Google Play Console: uploaded, blocked on 12-tester closed-testing requirement.
- F-Droid: metadata prepared, submission pending.
- Sideload: `C:\Users\HP\Downloads\SPASS Converter.apk`

Release keystore: `C:/Users/HP/Coding Projects/Android Key Stores/Spass Converter/Spass Converter Key`

---

## Agent Memory Protocol

Use this file as the standing project memory shared between Claude Code and Codex.

Rules:

1. Any meaningful code change, architecture decision, bug fix, regression, or workflow adjustment should be recorded here.
2. Each note should be prefixed with the agent name in brackets, for example `[Claude]` or `[Codex]`.
3. Prefer concise entries that explain what changed, why it changed, and any follow-up risk.
4. Keep older entries, including failed attempts and mistakes. Do not rewrite agent log history; append corrections and follow-up outcomes instead.
5. If the file's encoding gets damaged, normalize it back to plain ASCII or valid UTF-8 while preserving meaning.

Suggested note format:

```text
### YYYY-MM-DD
- [AgentName] What changed. Why it changed. Any follow-up note.
```

## Agent Log

### 2026-04-25
- [Claude] Added Sam's Club package mapping (`com.rfi.sams.android`) to AppUrlMapper — fallback heuristic produced wrong domain (`rfi.com`).
- [Claude] Added UTF-8 BOM to CsvExporter output — spreadsheet tools displayed mojibake without it.
- [Claude] Refactored ConverterViewModel: `password` moved into ConversionState, added `clearOnUserLeave()` — prevents sensitive state persisting after user leaves app.
- [Claude] Added `skipClearOnNextUserLeaveHint` gate in MainActivity — `onUserLeaveHint` was firing during app-initiated picker launches, clearing `fullCsv` before write and producing empty saved files.
- [Claude] Bumped versionCode 1→2, versionName 1.0→1.0.1.

### 2026-05-03
- [Claude] Fixed build.gradle.kts: moved hardcoded keystore path into `local.properties` as `RELEASE_STORE_FILE`; made entire signing config conditional on that key being present. Enables clean builds on F-Droid servers and other machines without the keystore.
- [Claude] Added MIT LICENSE file — required for F-Droid listing.
- [Claude] Added fastlane metadata under `fastlane/metadata/android/en-US/` — F-Droid uses this for app store listing display.
