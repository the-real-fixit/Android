package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: ProfileResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val appService = ApiClient.getAppService(application)
    
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    fun fetchProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val response = appService.getProfile()
                if (response.isSuccessful && response.body() != null) {
                    _profileState.value = ProfileState.Success(response.body()!!)
                } else {
                    _profileState.value = ProfileState.Error("No se pudo cargar el perfil")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}
