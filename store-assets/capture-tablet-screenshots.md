# Capture Real Tablet Screenshots

The PNGs in `tablet-7in/` and `tablet-10in/` have been re-encoded for better compatibility. If they still don't open, or you want fresh screenshots from the actual app, use this process.

## Option A: Physical Tablet

1. Install the app on your Samsung tablet (or any Android tablet).
2. Open the app → **Upload screen**: Take a screenshot (e.g., Power + Vol Down).
3. Select a .spass file, enter password, convert → **Success screen**: Take another screenshot.
4. Copy the images to your PC and save them as:
   - `tablet-7in/screenshot-tablet-7in-upload.png` and `screenshot-tablet-7in-success.png`
   - `tablet-10in/screenshot-tablet-10in-upload.png` and `screenshot-tablet-10in-success.png`
5. (Optional) Resize to match Play Store expectations if needed.

## Option B: Android Emulator (7" and 10" tablets)

### 1. Create tablet AVDs in Android Studio

- **Tools → Device Manager → Create Device**
- **7" tablet**: Category **Tablet** → e.g. "Nexus 7" or "7" WSVGA" (1024×600 or similar)
- **10" tablet**: e.g. "Pixel Tablet" or "10.1" WXGA" (1280×800 or similar)
- Use a recent system image (API 33+).

### 2. Build and run the app

```bash
cd "C:\Users\HP\Coding Projects\Android Studio Projects\Spassconverter"
.\gradlew installDebug
```

Or in Android Studio: **Run → Run 'app'** and pick the tablet AVD.

### 3. Capture screenshots with ADB

With the emulator running and the app open:

**Upload screen:**
```bash
adb exec-out screencap -p > tablet-7in-upload.png
```

**Success screen** (after running a conversion):
```bash
adb exec-out screencap -p > tablet-7in-success.png
```

Repeat for the 10" emulator, then move/copy the files into `tablet-7in/` and `tablet-10in/` with the correct names.

### 4. Verify

Open the PNGs in Windows Photos or your phone. They should display correctly.
