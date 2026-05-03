package com.stanley.spassconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.Arrays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConversionState(
    val fileName: String? = null,
    val fileSize: Long = 0,
    val isProcessing: Boolean = false,
    val fullCsv: String? = null,
    val summary: String = "",
    val totalEntries: Int = 0,
    val error: String? = null,
    val password: String = ""
)

class ConverterViewModel : ViewModel() {

    private val _state = MutableStateFlow(ConversionState())
    val state: StateFlow<ConversionState> = _state.asStateFlow()

    private var fileBytes: ByteArray? = null

    fun setFile(bytes: ByteArray, name: String, size: Long) {
        fileBytes = bytes
        _state.update { it.copy(fileName = name, fileSize = size, fullCsv = null, error = null, isProcessing = false) }
    }

    fun setPassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun convert() {
        val bytes = fileBytes ?: return
        val password = _state.value.password
        _state.update { it.copy(isProcessing = true, error = null, fullCsv = null) }

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val decrypted = SPassDecryptor().decrypt(bytes, password)
                val data = SPassParser().parse(decrypted)

                if (data.isEmpty) {
                    _state.update {
                        it.copy(isProcessing = false, error = "The file was decrypted but contains no data.")
                    }
                    return@launch
                }

                val fullCsv = CsvExporter.exportAll(data)

                _state.update {
                    it.copy(
                        isProcessing = false,
                        fullCsv = fullCsv,
                        summary = data.summary(),
                        totalEntries = data.totalEntries
                    )
                }
            } catch (e: SPassDecryptor.WrongPasswordException) {
                _state.update {
                    it.copy(isProcessing = false, error = "Wrong password. Please check and try again.")
                }
            } catch (e: SPassDecryptor.InvalidFileException) {
                _state.update {
                    it.copy(isProcessing = false, error = e.message ?: "Invalid .spass file.")
                }
            } catch (e: SPassParser.ParseException) {
                _state.update {
                    it.copy(isProcessing = false, error = "Could not read the file: ${e.message}")
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isProcessing = false, error = "Something went wrong: ${e.message}")
                }
            }
        }
    }

    fun clearSensitiveOutput() {
        _state.update { it.copy(fullCsv = null) }
    }

    /** Clears all sensitive data when the user leaves the app (Home, Back, app switch). */
    fun clearOnUserLeave() {
        fileBytes?.let { Arrays.fill(it, 0.toByte()) }
        fileBytes = null
        _state.value = ConversionState()
    }

    fun reset() {
        clearOnUserLeave()
    }

    override fun onCleared() {
        reset()
        super.onCleared()
    }
}
