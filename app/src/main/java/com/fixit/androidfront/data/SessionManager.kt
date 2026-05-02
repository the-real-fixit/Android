package com.fixit.androidfront.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "fixit_prefs"
        const val USER_TOKEN = "user_token"
    }

    /**
     * Guarda el token de autenticación (JWT).
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Obtiene el token guardado. Retorna nulo si no existe.
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Limpia la sesión actual.
     */
    fun clearSession() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }
}
