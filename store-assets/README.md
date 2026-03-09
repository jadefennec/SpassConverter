# Play Store Assets for SPASS Converter

Everything you need for Phase 4–5 of the Play Store submission.

## Ready to Use

| Asset | File | Specs |
|-------|------|-------|
| App icon | `app-icon-512.png` | 512×512px PNG, **max 1MB** — may need compression (see below) |
| Feature graphic | `feature-graphic-1024x500.png` | 1024×500px PNG/JPG |
| Short description | `short-description.txt` | Max 80 chars |
| Full description | `full-description.txt` | Max 4000 chars |

## What You Need to Provide

### Screenshots (minimum 2)

Take these on your phone or emulator:

1. **Upload screen** — The initial screen where users tap to select a .spass file  
   - Capture before a file is selected, or right after opening the app

2. **Success screen** — The screen after conversion completes  
   - Shows the success state with stats and preview/share options

**Requirements:** At least 320px on the short side. Phone screenshots are typically 1080×2340 or similar—Play Store will accept them.

**Tip:** Use power + volume down (or your device’s screenshot shortcut). Transfer to your PC and save as `screenshot-upload.png` and `screenshot-success.png` in this folder.

## Compressing the App Icon (if needed)

Play Store requires the icon to be under 1MB. If `app-icon-512.png` is larger:
- Use [TinyPNG.com](https://tinypng.com) to compress
- Or export from Android Studio (Image Asset) for a smaller file

## Alternative: Use Your Own App Icon

If you prefer the icon from your app exactly:

1. In Android Studio: **File → New → Image Asset**
2. Choose **Launcher Icons (Adaptive and Legacy)**
3. Use existing `ic_launcher_foreground` + `ic_launcher_background`
4. In the asset editor: **Resize** to 512×512 and export as PNG
5. Replace `app-icon-512.png` in this folder

## Upload Order in Play Console

1. Main store listing → Short description (paste from `short-description.txt`)
2. Main store listing → Full description (paste from `full-description.txt`)
3. Main store listing → App icon → Upload `app-icon-512.png`
4. Main store listing → Feature graphic → Upload `feature-graphic-1024x500.png`
5. Main store listing → Screenshots → Upload from subfolders:
   - **phone/** — `screenshot-upload.jpg`, `screenshot-success.jpg`
   - **tablet-7in/** — `screenshot-tablet-7in-upload.png`, `screenshot-tablet-7in-success.png`
   - **tablet-10in/** — `screenshot-tablet-10in-upload.png`, `screenshot-tablet-10in-success.png`
