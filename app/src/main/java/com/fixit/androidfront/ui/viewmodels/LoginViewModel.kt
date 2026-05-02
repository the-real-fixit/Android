package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.ApiClient
import com.fixit.androidfront.data.LoginRequest
import com.fixit.androidfront.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = ApiClient.getAuthService(application)
    private val sessionManager = SessionManager(application)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Los campos no pueden estar vacíos")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val response = authService.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    // Guardar el token en el SessionManager
                    sessionManager.saveAuthToken(loginResponse.accessToken)
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error("Credenciales inválidas. Inténtalo de nuevo.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
