package com.example.reviera
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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


data class MainCategory(
    val title: String,
    val description: String,
    val imageRes: Int, // Ресурс полноцветного изображения
    val subItems: List<String> // Список подэлементов
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: SpaViewModel, currentRoute: String) {
    var showPromotion by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    var selectedNavItem by remember { mutableStateOf(currentRoute) } // Синхронизация с текущим маршрутом

    val navItems = listOf(
        NavItem("Новости", "news", R.drawable.ic_news),
        NavItem("Главная", "home", R.drawable.ic_home),
        NavItem("Настройки", "settings", R.drawable.ic_settings)
    )

    // Список категорий с полноцветными изображениями (сохранили "Сервис")
    val categories = listOf(
        MainCategory(
            title = "Номера",
            description = "Уютные номера для отдыха",
            imageRes = R.drawable.ic_number, // Изображение номера
            subItems = listOf("Номер 1", "Номер 2", "Номер 3")
        ),
        MainCategory(
            title = "Отели",
            description = "Для отеля",
            imageRes = R.drawable.ic_hotel, // Изображение отеля
            subItems = listOf("Отель 1", "Отель 2", "Отель 3")
        ),
        MainCategory(
            title = "Рестораны",
            description = "Услуги по уходу за собой",
            imageRes = R.drawable.ic_restoraunt, // Изображение ресторана
            subItems = listOf("Ресторан 1", "Ресторан 2", "Ресторан 3")
        ),
        MainCategory(
            title = "Сервис",
            description = "Эксклюзивные программы",
            imageRes = R.drawable.ic_service, // Изображение сервиса
            subItems = listOf("Сервис 1", "Сервис 2", "Сервис 3")
        )
    )

    // Состояние для отслеживания раскрытия категорий
    var expandedCategory by remember { mutableStateOf<String?>(null) }

    val promotions by viewModel.promotions.collectAsState()

    LaunchedEffect(Unit) {
        showPromotion = true
        viewModel.loadPromotions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Riviera Sunrise",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp)
                                .clip(RoundedCornerShape(24.dp))
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.DarkGray)
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
                                            .size(64.dp)
                                            .align(Alignment.Center)
                                            .offset(y = (-16).dp, x = 30.dp)
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
                                            .offset(y = 14.dp, x = 30.dp)
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = navItem.iconRes),
                                            contentDescription = navItem.title,
                                            tint = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary.copy(
                                                alpha = 0.6f
                                            ),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            navItem.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary.copy(
                                                alpha = 0.6f
                                            ),
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
        },
        containerColor = Color.DarkGray // Темный фон экрана
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Фиксированное соотношение сторон 1:1 для квадратных карточек
                        .clickable {
                            expandedCategory = if (expandedCategory == category.title) null else category.title
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Полноцветное изображение (остается на месте)
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f) // Изображение занимает большую часть карточки
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Название с черным цветом текста
                        Text(
                            category.title,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Black, // Черный текст
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Раскрывающийся список с более плавной анимацией (без исчезновения изображения)
                        AnimatedVisibility(
                            visible = expandedCategory == category.title,
                            enter = expandVertically(
                                animationSpec = tween(durationMillis = 600)
                            ),
                            exit = shrinkVertically(
                                animationSpec = tween(durationMillis = 600)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                category.subItems.forEach { subItem ->
                                    Text(
                                        subItem,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black, // Черный текст
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable { /* Действие при нажатии на подэлемент */ }
                                    )
                                    Divider(color = Color.Black.copy(alpha = 0.2f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}







