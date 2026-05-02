package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CategoryState {
    object Loading : CategoryState()
    data class Success(
        val category: Category?,
        val jobPosts: List<JobPost>
    ) : CategoryState()
    data class Error(val message: String) : CategoryState()
}

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val appService = ApiClient.getAppService(application)
    
    private val _categoryState = MutableStateFlow<CategoryState>(CategoryState.Loading)
    val categoryState: StateFlow<CategoryState> = _categoryState.asStateFlow()

    fun fetchCategoryAds(categoryId: String) {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                // Fetch Categories to find the name of the current one
                val categoriesRes = appService.getCategories()
                var categoriesList = if (categoriesRes.isSuccessful) categoriesRes.body() ?: emptyList() else emptyList()
                
                if (categoriesList.isEmpty()) {
                    categoriesList = listOf(
                        Category(id = "c1", name = "Electricista"),
                        Category(id = "c2", name = "Plomería"),
                        Category(id = "c3", name = "Pintura"),
                        Category(id = "c4", name = "Carpintería"),
                        Category(id = "c5", name = "Mudanza"),
                        Category(id = "c6", name = "Jardinería"),
                        Category(id = "c7", name = "Técnico"),
                        Category(id = "c8", name = "Albañil"),
                        Category(id = "c9", name = "Limpieza")
                    )
                }
                
                val category = categoriesList.find { it.id == categoryId }

                // Fetch job posts for this category
                val feedRes = appService.getJobPosts(categoryId = categoryId)
                val feedList = if (feedRes.isSuccessful) feedRes.body() ?: emptyList() else emptyList()
                
                _categoryState.value = CategoryState.Success(
                    category = category,
                    jobPosts = feedList
                )

            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}
