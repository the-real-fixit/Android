package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class JobPostState {
    object Loading : JobPostState()
    data class Success(val jobPost: JobPost) : JobPostState()
    data class Error(val message: String) : JobPostState()
}

class JobPostViewModel(application: Application) : AndroidViewModel(application) {
    private val appService = ApiClient.getAppService(application)
    
    private val _jobPostState = MutableStateFlow<JobPostState>(JobPostState.Loading)
    val jobPostState: StateFlow<JobPostState> = _jobPostState.asStateFlow()

    fun fetchJobPost(id: String) {
        viewModelScope.launch {
            _jobPostState.value = JobPostState.Loading
            try {
                val response = appService.getJobPostById(id)
                if (response.isSuccessful && response.body() != null) {
                    _jobPostState.value = JobPostState.Success(response.body()!!)
                } else {
                    _jobPostState.value = JobPostState.Error("Anuncio no encontrado")
                }
            } catch (e: Exception) {
                _jobPostState.value = JobPostState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}
