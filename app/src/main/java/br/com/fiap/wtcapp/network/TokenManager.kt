package br.com.fiap.wtcapp.network

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveSession(token: String, role: String, userId: String) {
        prefs.edit()
            .putString("jwt_token", token)
            .putString("user_role", role)
            .putString("user_id", userId)
            .apply()
    }

    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun getRole(): String? = prefs.getString("user_role", "CLIENTE")
    fun getUserId(): String? = prefs.getString("user_id", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}