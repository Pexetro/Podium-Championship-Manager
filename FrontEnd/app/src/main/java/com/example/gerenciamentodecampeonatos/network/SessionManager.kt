package com.example.gerenciamentodecampeonatos.utils

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun salvarIdUsuario(id: Long) {
        prefs.edit().putLong("ID_USUARIO_LOGADO", id).apply()
    }

    fun obterIdUsuario(): Long {
        return prefs.getLong("ID_USUARIO_LOGADO", 0L)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun estaLogado(): Boolean {
        return obterIdUsuario() > 0L
    }
}