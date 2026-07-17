package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.ApiClient
import com.fixit.androidfront.data.JobPostCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

sealed class CreateJobPostState {
    object Idle : CreateJobPostState()
    object Loading : CreateJobPostState()
    data class Success(val message: String) : CreateJobPostState()
    data class Error(val message: String) : CreateJobPostState()
}

class CreateJobPostViewModel(application: Application) : AndroidViewModel(application) {
    private val appService = ApiClient.getAppService(application)
    
    private val _state = MutableStateFlow<CreateJobPostState>(CreateJobPostState.Idle)
    val state: StateFlow<CreateJobPostState> = _state.asStateFlow()

    fun createJobPost(
        title: String,
        description: String,
        budget: String,
        department: String,
        municipality: String,
        address: String,
        notes: String,
        categoryId: String,
        imageUris: List<Uri>
    ) {
        viewModelScope.launch {
            _state.value = CreateJobPostState.Loading
            try {
                val uploadedUrls = mutableListOf<String>()

                if (imageUris.isNotEmpty()) {
                    val sigResponse = appService.getUploadSignature()
                    if (sigResponse.isSuccessful && sigResponse.body() != null) {
                        val sigData = sigResponse.body()!!
                        
                        val client = OkHttpClient()
                        
                        for (uri in imageUris) {
                            // Copy URI to temp file
                            val tempFile = File.createTempFile("upload", ".jpg", getApplication<Application>().cacheDir)
                            getApplication<Application>().contentResolver.openInputStream(uri)?.use { input ->
                                FileOutputStream(tempFile).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            
                            val requestBody = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", tempFile.name, tempFile.asRequestBody("image/*".toMediaTypeOrNull()))
                                .addFormDataPart("api_key", sigData.apiKey)
                                .addFormDataPart("timestamp", sigData.timestamp.toString())
                                .addFormDataPart("signature", sigData.signature)
                                .addFormDataPart("folder", sigData.folder)
                                .build()
                                
                            val request = Request.Builder()
                                .url("https://api.cloudinary.com/v1_1/${sigData.cloudName}/${sigData.resourceType}/upload")
                                .post(requestBody)
                                .build()
                                
                            val response = client.newCall(request).execute()
                            if (response.isSuccessful) {
                                val responseBody = response.body?.string()
                                if (responseBody != null) {
                                    val json = JSONObject(responseBody)
                                    val secureUrl = json.getString("secure_url")
                                    uploadedUrls.add(secureUrl)
                                }
                            }
                        }
                    }
                }
                
                // Combine address and notes into location
                val fullLocation = if (notes.isNotBlank()) "$address - Notas: $notes" else address

                val request = JobPostCreateRequest(
                    title = title,
                    description = description,
                    budget = budget.ifBlank { null },
                    location = fullLocation,
                    department = department,
                    municipality = municipality,
                    lat = null,
                    lng = null,
                    categoryId = categoryId,
                    photos = uploadedUrls
                )
                
                val response = appService.createJobPost(request)
                if (response.isSuccessful) {
                    _state.value = CreateJobPostState.Success("Anuncio publicado exitosamente")
                } else {
                    _state.value = CreateJobPostState.Error("Error al publicar el anuncio: ${response.message()}")
                }

            } catch (e: Exception) {
                _state.value = CreateJobPostState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
