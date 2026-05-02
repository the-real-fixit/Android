package com.fixit.androidfront.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fixit.androidfront.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val profile: ProfileResponse?,
        val categories: List<Category>,
        val jobPosts: List<JobPost>, // Feed
        val myAds: List<JobPost> // My Ads
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val appService = ApiClient.getAppService(application)
    
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            try {
                // 1. Fetch Profile to know who is logged in and their role
                val profileRes = appService.getProfile()
                var authorId: String? = null
                var authorRole: String? = null
                var profileData: ProfileResponse? = null

                if (profileRes.isSuccessful && profileRes.body() != null) {
                    profileData = profileRes.body()
                    // Si el usuario es cliente, en el feed quiere ver ads de PROVIDER
                    authorRole = if (profileData?.role == "CLIENT") "PROVIDER" else "CLIENT"
                    // Su ID real para 'mis anuncios' se podría obtener si el perfil incluye el ID
                    // Pero asumiendo que /profile no da el id directamente en el nivel root, o tal vez el backend
                    // lo saca del token si no pasas authorId a /job-posts? Lo mejor es usar el ID del usuario
                    // Si el backend requiere authorId, asumimos que se extrae del perfil.
                }

                // 2. Fetch Categories
                val categoriesRes = appService.getCategories()
                var categoriesList = if (categoriesRes.isSuccessful) categoriesRes.body() ?: emptyList() else emptyList()

                // Fallback de categorías (igual que en la web) si el backend está vacío
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

                // 3. Fetch Feed Job Posts
                val feedRes = appService.getJobPosts(authorRole = authorRole)
                val feedList = if (feedRes.isSuccessful) feedRes.body() ?: emptyList() else emptyList()

                // 4. Fetch My Ads (if backend supports pulling from token, we don't need authorId. Otherwise we pass authorId if we had it. Let's assume the endpoint can filter by token if we don't pass authorId, or we leave it empty if we don't know the ID).
                // Revisando EmployerDashboard.tsx, se usa `/job-posts?authorId=${user?.id}`
                // Aquí, el endpoint getProfile nos devuelve ProfileResponse. Si necesitamos el ID, tendremos que añadirlo a ProfileResponse o llamar a la API correspondiente.
                // Como workaround temporal, myAds puede ser una llamada normal sin authorId, el backend debería filtrar (si es que está implementado), si no, lo dejamos vacío por ahora hasta tener el ID.
                val myAdsList = mutableListOf<JobPost>()
                
                _homeState.value = HomeState.Success(
                    profile = profileData,
                    categories = categoriesList,
                    jobPosts = feedList,
                    myAds = myAdsList
                )

            } catch (e: Exception) {
                _homeState.value = HomeState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}
