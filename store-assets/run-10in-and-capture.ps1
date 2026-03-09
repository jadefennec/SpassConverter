# Run 10" tablet emulator, install app, push dummy spass, launch - for easy screenshots
# Run from store-assets folder or: powershell -ExecutionPolicy Bypass -File run-10in-and-capture.ps1
$ErrorActionPreference = "Stop"
$sdk = "$env:LOCALAPPDATA\Android\Sdk"
$env:Path = "$sdk\platform-tools;$sdk\emulator;$env:Path"
$here = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "Starting 10 inch tablet emulator..."
Start-Process -FilePath "$sdk\emulator\emulator.exe" -ArgumentList "-avd", "Tablet_10_inch" -WindowStyle Normal

Write-Host "Waiting for boot (up to ~4 min)..."
for ($i = 0; $i -lt 80; $i++) {
    $out = adb devices 2>$null
    if ($out -match "emulator.*\tdevice") {
        Write-Host "Emulator ready."
        break
    }
    Start-Sleep -Seconds 3
    Write-Host "  $($i * 3)s..."
}
$dev = adb devices | Select-String "emulator.*device"
if (-not $dev) {
    Write-Host "Emulator did not come up in time. Check the emulator window - first boot can take 3-5 min."
    exit 1
}

Write-Host "Pushing dummy_screenshot.spass..."
adb push (Join-Path $here "dummy_screenshot.spass") /sdcard/

Write-Host "Installing SPASS Converter..."
Push-Location (Join-Path $here "..")
& .\gradlew.bat installDebug -q
Pop-Location

Write-Host "Launching app..."
adb shell am start -n com.stanley.spassconverter/.MainActivity

Write-Host ""
Write-Host "Done. To capture screenshots:"
Write-Host "  1. Upload screen: already shown"
Write-Host "  2. Tap upload -> pick dummy_screenshot.spass from Internal storage (root)"
Write-Host "  3. Password: test123"
Write-Host "  4. Convert -> Success screen"
