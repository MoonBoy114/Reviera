package com.example.reviera

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reviera.data.viewmodel.SpaViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SpaViewModel, currentRoute: String) {
    var darkTheme by remember { mutableStateOf(true) }
    var selectedNavItem by remember { mutableStateOf(currentRoute) } // Синхронизация с текущим маршрутом
    val promotions by viewModel.promotions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
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
                    val navItems = listOf(
                        NavItem("Новости", "news", R.drawable.ic_news),
                        NavItem("Главная", "home", R.drawable.ic_home),
                        NavItem("Настройки", "settings", R.drawable.ic_settings)
                    )
                    navItems.forEachIndexed { index, navItem ->
                        val isSelected = selectedNavItem == navItem.route
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
                                if (selected) {
                                    Card(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .align(Alignment.Center)
                                            .offset(y = (-16).dp, x = 35.dp)
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
                                            .offset(y = 14.dp, x = 35.dp)
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = navItem.iconRes),
                                            contentDescription = navItem.title,
                                            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            navItem.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Настройки приложения", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))

            // Переключатель темы
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Темная тема", color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = darkTheme,
                    onCheckedChange = { darkTheme = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}

private val navItems = listOf(
    NavItem("Новости", "news", R.drawable.ic_news),
    NavItem("Главная", "home", R.drawable.ic_home),
    NavItem("Настройки", "settings", R.drawable.ic_settings)
)