package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.ApiClient
import com.fixit.androidfront.data.RegisterRequest
import com.fixit.androidfront.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = ApiClient.getAuthService(application)
    private val sessionManager = SessionManager(application)

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun register(email: String, pass: String, name: String, role: String) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val response = authService.register(RegisterRequest(email, pass, name, role))
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    sessionManager.saveAuthToken(loginResponse.accessToken)
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(response.errorBody()?.string() ?: "Error al registrarse")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}
