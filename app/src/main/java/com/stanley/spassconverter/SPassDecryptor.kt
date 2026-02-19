package com.stanley.spassconverter

import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class SPassDecryptor {

    class InvalidFileException(message: String) : Exception(message)
    class WrongPasswordException : Exception("Incorrect password")

    fun decrypt(fileBytes: ByteArray, password: String): String {
        val decoded = try {
            java.util.Base64.getDecoder().decode(
                String(fileBytes, Charsets.US_ASCII).trim()
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidFileException("Not a valid .spass file — failed to decode Base64")
        }

        if (decoded.size < SALT_LEN + IV_LEN + AES_BLOCK) {
            throw InvalidFileException("File too small (${decoded.size} bytes)")
        }

        val salt = decoded.copyOfRange(0, SALT_LEN)
        val iv = decoded.copyOfRange(SALT_LEN, SALT_LEN + IV_LEN)
        val encrypted = decoded.copyOfRange(SALT_LEN + IV_LEN, decoded.size)

        if (encrypted.size % AES_BLOCK != 0) {
            throw InvalidFileException("Corrupted file — encrypted data is not block-aligned")
        }

        val keySpec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_BITS)
        val key: ByteArray
        try {
            key = SecretKeyFactory.getInstance(PBKDF2_ALGO).generateSecret(keySpec).encoded
        } finally {
            keySpec.clearPassword()
        }

        try {
            val cipher = Cipher.getInstance(CIPHER_ALGO)
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))

            val plaintext = try {
                cipher.doFinal(encrypted)
            } catch (e: javax.crypto.BadPaddingException) {
                throw WrongPasswordException()
            } catch (e: javax.crypto.IllegalBlockSizeException) {
                throw InvalidFileException("Corrupted file — decryption failed")
            }

            val result = String(plaintext, Charsets.UTF_8)
            Arrays.fill(plaintext, 0.toByte())
            return result
        } finally {
            Arrays.fill(key, 0.toByte())
            Arrays.fill(salt, 0.toByte())
            Arrays.fill(iv, 0.toByte())
        }
    }

    companion object {
        private const val SALT_LEN = 20
        private const val IV_LEN = 16
        private const val AES_BLOCK = 16
        private const val ITERATIONS = 70_000
        private const val KEY_BITS = 256
        private const val PBKDF2_ALGO = "PBKDF2WithHmacSHA256"
        private const val CIPHER_ALGO = "AES/CBC/PKCS5Padding"
    }
}
