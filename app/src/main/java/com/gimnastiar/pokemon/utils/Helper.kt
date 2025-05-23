package com.gimnastiar.pokemon.utils

import android.content.Context
import android.net.ConnectivityManager
import java.security.MessageDigest

object Helper {

    fun hashPassword(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun generateDummyToken(email: String): String {
        val raw = "$email-${System.currentTimeMillis()}"
        return raw.hashCode().toString()
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnectedOrConnecting == true
    }

}