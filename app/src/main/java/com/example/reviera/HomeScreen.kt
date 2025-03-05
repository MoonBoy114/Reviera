package com.example.reviera
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reviera.data.viewmodel.SpaViewModel


data class ServiceItem(val iconRes: Int, val title: String, val route: String)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: SpaViewModel, currentRoute: String) {
    var showPromotion by remember { mutableStateOf(false) }
    val services = listOf(
        ServiceItem(R.drawable.ic_clean, "Клининг", "cleaning"),
        ServiceItem(R.drawable.ic_tv, "ТВ", "tv"),
        ServiceItem(R.drawable.ic_wifi, "Wi-Fi", "wifi"),
        ServiceItem(R.drawable.ic_safe, "Сейф", "safe"),
        ServiceItem(R.drawable.ic_food, "Ресторан", "restaurant"),
        ServiceItem(R.drawable.ic_call, "Контакты", "contacts"),
        ServiceItem(R.drawable.ic_spa, "SPA", "spa"),
        ServiceItem(R.drawable.ic_menu, "Меню", "menu")
    )

    val promotions by viewModel.promotions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    var selectedNavItem by remember { mutableStateOf(currentRoute) } // Синхронизация с текущим маршрутом

    val navItems = listOf(
        NavItem("Новости", "news", R.drawable.ic_news),
        NavItem("Главная", "home", R.drawable.ic_home),
        NavItem("Настройки", "settings", R.drawable.ic_settings)
    )

    LaunchedEffect(Unit) {
        showPromotion = true
        viewModel.loadPromotions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Riviera Sunrise",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .width(200.dp) // Фиксированная ширина
                                .height(48.dp)
                                .clip(RoundedCornerShape(24.dp)) // Закругленные углы
                                .onFocusChanged { isSearchFocused = it.isFocused },
                            placeholder = {
                                Text(
                                    "Поиск",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            },
                            trailingIcon = if (searchQuery.isNotEmpty()) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        modifier = Modifier.clickable { searchQuery = "" },
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            } else null,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            ),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { /* Handle search */ })
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Общая высота для выступающего круга
            ) {
                // Оранжевая полоса
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
                        .align(Alignment.BottomCenter)
                )

                // Навигационные элементы
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEachIndexed { index, navItem ->
                        val isSelected = selectedNavItem == navItem.route
                        val isCenterItem = index == 1 // "Главная" строго по центру (индекс 1 из 3 элементов)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedNavItem = navItem.route
                                    navController.navigate(navItem.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                        ) {
                            Crossfade(
                                targetState = isSelected,
                                animationSpec = tween(durationMillis = 300),
                                label = "NavItemAnimation"
                            ) { selected ->
                                if (selected && isCenterItem) {
                                    Card(
                                        modifier = Modifier
                                            .size(64.dp) // Увеличенный размер
                                            .align(Alignment.Center)
                                            .offset(y = (-16).dp, x = 35.dp) // Выступает над полосой
                                            .shadow(8.dp, CircleShape),
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                painter = painterResource(id = navItem.iconRes),
                                                contentDescription = navItem.title,
                                                tint = MaterialTheme.colorScheme.onSecondary,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Text(
                                                navItem.title,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSecondary,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .size(64.dp)
                                            .offset(y = 14.dp, x = 35.dp) // Сдвигаем неактивные иконки вниз
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = navItem.iconRes),
                                            contentDescription = navItem.title,
                                            tint = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            navItem.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            // Баннер
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.family_image),
                    contentDescription = "Family Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_disco),
                        contentDescription = "Disco",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Акция
            AnimatedVisibility(
                visible = showPromotion && promotions.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1000))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                promotions.firstOrNull()?.title ?: "Продлите свой отдых со скидкой 15%",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                "Ограничено по времени",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_hand),
                            contentDescription = "Hand Icon",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Список услуг
            Text(
                "Виртуальный помощник в номерах",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(services) { item ->
                    Card(
                        modifier = Modifier
                            .clickable { navController.navigate(item.route) }
                            .aspectRatio(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                item.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}



