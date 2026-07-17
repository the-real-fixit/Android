package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fixit.androidfront.R
import com.fixit.androidfront.data.Category
import com.fixit.androidfront.data.JobPost
import com.fixit.androidfront.ui.theme.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import com.fixit.androidfront.ui.viewmodels.HomeState
import com.fixit.androidfront.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCategory: (String) -> Unit = {},
    onNavigateToCreateJob: () -> Unit = {},
    onNavigateToJobDetail: (String) -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onLogout: () -> Unit = {},
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeState by homeViewModel.homeState.collectAsState()
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Request location permissions once on first launch
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                      permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (!granted) {
            // Permission denied — show informational snackbar
        }
    }

    // Launch location permission request AND initial data load together
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        homeViewModel.fetchHomeData()
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            homeViewModel.fetchHomeData(isRefresh = true)
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToProfile()
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                HomeTopBar(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateJob,
                containerColor = PrimaryYellow,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.home_cd_add_post))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            when (homeState) {
                is HomeState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = PrimaryYellow) }
                }
                is HomeState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(text = (homeState as HomeState.Error).message, color = Color.Red) }
                }
                is HomeState.Success -> {
                    val state = homeState as HomeState.Success

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundGray),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        item {
                            val userName = state.profile?.name ?: stringResource(R.string.home_welcome_prefix)
                            val userRole = state.profile?.role ?: ""
                            WelcomeBanner(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                name = userName,
                                role = userRole
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        item {
                            PopularCategories(
                                categories = state.categories,
                                onNavigateToCategory = onNavigateToCategory
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        item {
                            HomeTabsAndAds(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                jobPosts = state.jobPosts,
                                myAds = state.myAds,
                                onNavigateToJobDetail = onNavigateToJobDetail
                            )
                        }
                    }
                }
                else -> Unit
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
    }
}

@Composable
fun HomeTopBar(onOpenDrawer: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryYellow)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onOpenDrawer) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.home_app_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(SurfaceWhite, RoundedCornerShape(8.dp)),
            placeholder = { Text(stringResource(R.string.home_search_placeholder), color = TextGray) },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.home_search_cd),
                    tint = TextGray
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun WelcomeBanner(modifier: Modifier = Modifier, name: String, role: String) {
    val description = if (role == "CLIENT")
        stringResource(R.string.home_welcome_client_desc)
    else
        stringResource(R.string.home_welcome_provider_desc)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.home_welcome_prefix) + name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp, color = TextGray)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun getIconForCategory(categoryName: String): ImageVector {
    return when (categoryName) {
        "Electricista" -> Icons.Outlined.Bolt
        "Plomería" -> Icons.Outlined.Build
        "Pintura" -> Icons.Outlined.FormatPaint
        "Carpintería" -> Icons.Outlined.Handyman
        "Mudanza" -> Icons.Outlined.LocalShipping
        "Jardinería" -> Icons.Outlined.Eco
        "Técnico" -> Icons.Outlined.Computer
        "Albañil" -> Icons.Outlined.Home
        "Paseo de mascotas" -> Icons.Outlined.Pets
        "Cuidado de mascotas" -> Icons.Outlined.Pets
        "Limpieza" -> Icons.Outlined.Sanitizer
        "Fotografía" -> Icons.Outlined.PhotoCamera
        "Clases" -> Icons.Outlined.School
        else -> Icons.Outlined.Build
    }
}

@Composable
fun PopularCategories(categories: List<Category>, onNavigateToCategory: (String) -> Unit = {}) {
    Column {
        Text(
            text = stringResource(R.string.home_popular_categories),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (categories.isEmpty()) {
            Text(
                stringResource(R.string.home_no_categories),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = TextGray
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    Column(
                        modifier = Modifier.width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .shadow(2.dp, CircleShape)
                                .clip(CircleShape)
                                .background(PrimaryYellow)
                                .clickable { onNavigateToCategory(category.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getIconForCategory(category.name),
                                contentDescription = category.name,
                                modifier = Modifier.size(32.dp),
                                tint = TextDark
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = category.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTabsAndAds(
    modifier: Modifier = Modifier,
    jobPosts: List<JobPost>,
    myAds: List<JobPost>,
    onNavigateToJobDetail: (String) -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val currentList = if (selectedTabIndex == 0) jobPosts else myAds

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            TabItem(
                text = stringResource(R.string.home_tab_search),
                isSelected = selectedTabIndex == 0,
                modifier = Modifier.clickable { selectedTabIndex = 0 }
            )
            Spacer(modifier = Modifier.width(24.dp))
            TabItem(
                text = stringResource(R.string.home_tab_mine),
                isSelected = selectedTabIndex == 1,
                modifier = Modifier.clickable { selectedTabIndex = 1 }
            )
        }

        HorizontalDivider(color = OutlineGray)
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (selectedTabIndex == 0)
                stringResource(R.string.home_section_available)
            else
                stringResource(R.string.home_section_mine),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (currentList.isEmpty()) {
            Text(
                stringResource(R.string.home_empty_posts),
                color = TextGray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                currentList.forEach { post ->
                    AdCard(
                        title = post.title,
                        author = post.author.name,
                        rating = post.author.profile?.rating?.toString() ?: "N/A",
                        price = post.budget?.toString(),
                        categoryName = post.category?.name,
                        onClick = { onNavigateToJobDetail(post.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PrimaryYellow else TextGray
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(3.dp)
                    .background(PrimaryYellow)
            )
        }
    }
}

@Composable
fun AdCard(
    title: String,
    author: String,
    rating: String,
    price: String? = null,
    categoryName: String? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(OutlineGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Image, contentDescription = null, tint = TextGray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = author, fontSize = 14.sp, color = TextGray)
                }
                if (categoryName != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = categoryName, fontSize = 12.sp, color = PrimaryYellow, fontWeight = FontWeight.Bold)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = stringResource(R.string.home_rating_cd),
                        tint = PrimaryYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = rating, fontWeight = FontWeight.Bold, color = TextDark)
                }
                if (price != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Q$price", fontWeight = FontWeight.Bold, color = Color(0xFF166534))
                }
            }
        }
    }
}
