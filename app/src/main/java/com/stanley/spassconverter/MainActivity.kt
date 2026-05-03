package com.stanley.spassconverter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stanley.spassconverter.ui.theme.BgField
import com.stanley.spassconverter.ui.theme.BgFileCard
import com.stanley.spassconverter.ui.theme.BgPage
import com.stanley.spassconverter.ui.theme.BgPrivacyChip
import com.stanley.spassconverter.ui.theme.BgStatChip
import com.stanley.spassconverter.ui.theme.BgUploadIcon
import com.stanley.spassconverter.ui.theme.BgSuccessZone
import com.stanley.spassconverter.ui.theme.BgUploadZone
import com.stanley.spassconverter.ui.theme.BorderDefault
import com.stanley.spassconverter.ui.theme.BorderSuccess
import com.stanley.spassconverter.ui.theme.BorderStat
import com.stanley.spassconverter.ui.theme.BtnPrimaryBg
import com.stanley.spassconverter.ui.theme.BtnPrimaryText
import com.stanley.spassconverter.ui.theme.BtnSecondaryBorder
import com.stanley.spassconverter.ui.theme.BtnSecondaryText
import com.stanley.spassconverter.ui.theme.CheckCircleBg
import com.stanley.spassconverter.ui.theme.DmMono
import com.stanley.spassconverter.ui.theme.DmSans
import com.stanley.spassconverter.ui.theme.FileIconBg
import com.stanley.spassconverter.ui.theme.IconChevron
import com.stanley.spassconverter.ui.theme.IconField
import com.stanley.spassconverter.ui.theme.IconFile
import com.stanley.spassconverter.ui.theme.IconPrivacy
import com.stanley.spassconverter.ui.theme.IconSuccess
import com.stanley.spassconverter.ui.theme.IconUpload
import com.stanley.spassconverter.ui.theme.SpassConverterTheme
import com.stanley.spassconverter.ui.theme.TextDisclaimer
import com.stanley.spassconverter.ui.theme.TextFileName
import com.stanley.spassconverter.ui.theme.TextFileMeta
import com.stanley.spassconverter.ui.theme.TextLabel
import com.stanley.spassconverter.ui.theme.TextPrimary
import com.stanley.spassconverter.ui.theme.TextPrivacy
import com.stanley.spassconverter.ui.theme.TextSecondary
import com.stanley.spassconverter.ui.theme.TextStatLabel
import com.stanley.spassconverter.ui.theme.TextStatNum
import com.stanley.spassconverter.ui.theme.TextSuccessSub
import com.stanley.spassconverter.ui.theme.TextSuccessTitle
import com.stanley.spassconverter.ui.theme.TextTertiary
import java.io.File
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    private var skipClearOnNextUserLeaveHint = false

    fun markAppInitiatedExternalNavigation() {
        skipClearOnNextUserLeaveHint = true
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (skipClearOnNextUserLeaveHint) {
            skipClearOnNextUserLeaveHint = false
            return
        }
        ViewModelProvider(this)[ConverterViewModel::class.java].clearOnUserLeave()
        File(cacheDir, "passwords_export.csv").delete()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                File(cacheDir, "passwords_export.csv").delete()
            }
        })

        enableEdgeToEdge()
        setContent {
            SpassConverterTheme {
                SPassConverterApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SPassConverterApp(viewModel: ConverterViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val bytes = context.contentResolver.openInputStream(it)?.use { s -> s.readBytes() }
            if (bytes != null) {
                val (name, size) = getFileInfo(context, it)
                viewModel.setFile(bytes, name, size)
            }
        }
    }

    val csvSaver = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        uri?.let {
            val csv = state.fullCsv ?: return@let
            context.contentResolver.openOutputStream(it)?.use { out ->
                out.write(csv.toByteArray(Charsets.UTF_8))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 48.dp, bottom = 32.dp)
    ) {
        AppHeader()

        when {
            state.fullCsv != null -> SuccessScreen(
                state = state,
                onSave = {
                    (context as? MainActivity)?.markAppInitiatedExternalNavigation()
                    csvSaver.launch("spass_export.csv")
                },
                onConvertAnother = {
                    File(context.cacheDir, "passwords_export.csv").delete()
                    viewModel.reset()
                },
                onPreview = {
                    val csv = state.fullCsv ?: return@SuccessScreen
                    val file = File(context.cacheDir, "passwords_export.csv")
                    file.writeText(csv, Charsets.UTF_8)
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "text/csv")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    (context as? MainActivity)?.markAppInitiatedExternalNavigation()
                    context.startActivity(Intent.createChooser(intent, null))
                }
            )
            else -> UploadScreen(
                state = state,
                password = state.password,
                onPasswordChange = { viewModel.setPassword(it) },
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                onConvert = { viewModel.convert() },
                onSelectFile = {
                    (context as? MainActivity)?.markAppInitiatedExternalNavigation()
                    filePicker.launch(arrayOf("*/*"))
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Your file is processed locally\nand never leaves this device",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = DmMono,
            fontSize = 10.sp,
            letterSpacing = 0.05.sp,
            color = TextDisclaimer,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AppHeader() {
    Text(
        "SPASS\nConverter",
        style = MaterialTheme.typography.headlineMedium,
        fontFamily = DmSans,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        fontSize = 23.sp,
        letterSpacing = (-0.02 * 23).sp,
        color = TextPrimary,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        "Samsung → CSV export",
        style = MaterialTheme.typography.bodyMedium,
        fontFamily = DmSans,
        fontSize = 12.sp,
        color = TextTertiary,
        modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
    )
}

@Composable
private fun UploadScreen(
    state: ConversionState,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onConvert: () -> Unit,
    onSelectFile: () -> Unit
) {
    AnimatedVisibility(visible = state.error != null, enter = fadeIn(), exit = fadeOut()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                state.error ?: "",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(148.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(BgUploadZone)
            .border(1.5.dp, BorderDefault, RoundedCornerShape(20.dp))
            .clickable { onSelectFile() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BgUploadIcon),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Upload, contentDescription = null, tint = IconUpload, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                if (state.fileName != null) state.fileName!! else "Tap to upload .spass file",
                fontFamily = DmSans,
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                if (state.fileSize > 0) formatFileSize(state.fileSize) else ".SPASS · MAX 50MB",
                fontFamily = DmMono,
                fontSize = 10.sp,
                letterSpacing = 0.08.sp,
                color = TextDisclaimer
            )
        }
    }
    Spacer(modifier = Modifier.height(18.dp))

    Text(
        "EXPORT PASSWORD",
        fontFamily = DmMono,
        fontSize = 10.sp,
        letterSpacing = 0.12.sp,
        color = TextLabel,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BgField)
            .border(1.dp, BorderDefault, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Lock, contentDescription = null, tint = IconField, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text("··········", fontFamily = DmMono, fontSize = 14.sp, letterSpacing = 0.18.sp, color = TextDisclaimer) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrectEnabled = false),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                cursorColor = TextPrimary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedPlaceholderColor = TextDisclaimer,
                unfocusedPlaceholderColor = TextDisclaimer
            )
        )
        IconButton(onClick = onTogglePasswordVisibility, modifier = Modifier.size(32.dp)) {
            Icon(
                if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = null,
                tint = IconField,
                modifier = Modifier.size(16.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(18.dp))

    val convertEnabled = state.fileName != null && password.isNotEmpty() && !state.isProcessing
    Button(
        onClick = onConvert,
        enabled = convertEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .alpha(if (convertEnabled) 1f else 0.25f),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BtnPrimaryBg),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (state.isProcessing) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = BtnPrimaryText)
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = BtnPrimaryText, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text("Convert to CSV", fontFamily = DmSans, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 14.sp, color = BtnPrimaryText)
    }
}

@Composable
private fun SuccessScreen(
    state: ConversionState,
    onSave: () -> Unit,
    onConvertAnother: () -> Unit,
    onPreview: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgSuccessZone),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSuccess)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(CheckCircleBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = IconSuccess, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Conversion complete", fontFamily = DmSans, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 14.sp, color = TextSuccessTitle)
                    Text("Decrypted · ${state.totalEntries} entries found", fontFamily = DmMono, fontSize = 10.sp, letterSpacing = 0.05.sp, color = TextSuccessSub, modifier = Modifier.padding(top = 2.dp))
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                StatChip("${state.totalEntries}", "Entries", modifier = Modifier.weight(1f))
                StatChip(state.summary.split(",").size.toString(), "Fields", modifier = Modifier.weight(1f))
                StatChip(formatFileSize(state.fileSize), "Size", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgPrivacyChip)
                    .border(1.dp, BorderStat, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = IconPrivacy, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contents are not read or stored.\nProcessed entirely on-device.", fontFamily = DmMono, fontSize = 10.sp, letterSpacing = 0.03.sp, color = TextPrivacy, lineHeight = 15.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPreview() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgFileCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDefault)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FileIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = IconFile, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("passwords_export.csv", fontFamily = DmSans, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 13.sp, color = TextFileName)
                Text("tap to preview", fontFamily = DmMono, fontSize = 10.sp, letterSpacing = 0.04.sp, color = TextFileMeta, modifier = Modifier.padding(top = 2.dp))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = IconChevron, modifier = Modifier.size(14.dp))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onSave,
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BtnPrimaryBg)
    ) {
        Icon(Icons.Default.Download, contentDescription = null, tint = BtnPrimaryText, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Save to Storage", fontFamily = DmSans, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 14.sp, color = BtnPrimaryText)
    }
    Button(
        onClick = onConvertAnother,
        modifier = Modifier.fillMaxWidth().height(44.dp).padding(top = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = BtnSecondaryText),
        border = androidx.compose.foundation.BorderStroke(1.dp, BtnSecondaryBorder)
    ) {
        Icon(Icons.Default.Refresh, contentDescription = null, tint = BtnSecondaryText, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Convert another file", fontFamily = DmSans, fontSize = 12.sp, color = BtnSecondaryText)
    }
}

@Composable
private fun StatChip(num: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgStatChip)
            .border(1.dp, BorderStat, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(num, fontFamily = DmMono, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 18.sp, color = TextStatNum)
            Text(label, fontFamily = DmMono, fontSize = 9.sp, letterSpacing = 0.10.sp, color = TextStatLabel, modifier = Modifier.padding(top = 1.dp))
        }
    }
}

private fun getFileInfo(context: Context, uri: Uri): Pair<String, Long> {
    var name = "file.spass"
    var size = 0L
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (cursor.moveToFirst()) {
            if (nameIdx >= 0) name = cursor.getString(nameIdx) ?: name
            if (sizeIdx >= 0) size = cursor.getLong(sizeIdx)
        }
    }
    return name to size
}

private fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "—"
    val units = arrayOf("B", "KB", "MB")
    val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt().coerceAtMost(units.lastIndex)
    return "${DecimalFormat("#,##0.#").format(bytes / Math.pow(1024.0, digitGroups.toDouble()))} ${units[digitGroups]}"
}
