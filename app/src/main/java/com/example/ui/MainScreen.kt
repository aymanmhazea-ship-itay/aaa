package com.example.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.DownloadItem
import com.example.data.TechItem

// Vibrant Premium Theme Palette
val DarkBackground = Color(0xFF0D0B14)
val SurfaceCard = Color(0xFF161322)
val AccentColor = Color(0xFF8C52FF)
val GlowColor = Color(0xFF39FF14) // Neon Cyber Green
val GoldColor = Color(0xFFFFD700)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA5A1B8)
val DividerColor = Color(0xFF26223B)

val GradientStart = Color(0xFF1E1035)
val GradientEnd = Color(0xFF0B0813)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: XDownloaderViewModel) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(0) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Downloading,
                                contentDescription = "اكس عربي",
                                tint = AccentColor,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "اكس عربي",
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp,
                                color = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = DarkBackground,
                        titleContentColor = TextPrimary
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = SurfaceCard,
                    tonalElevation = 8.dp,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentColor,
                            selectedTextColor = AccentColor,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = DividerColor
                        ),
                        icon = { Icon(Icons.Default.Download, contentDescription = "الرئيسية") },
                        label = { Text("تحميل", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentColor,
                            selectedTextColor = AccentColor,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = DividerColor
                        ),
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "التقنيات") },
                        label = { Text("التقنيات", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentColor,
                            selectedTextColor = AccentColor,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = DividerColor
                        ),
                        icon = { Icon(Icons.Default.History, contentDescription = "السجل") },
                        label = { Text("التحميلات", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 3,
                        onClick = { currentTab = 3 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentColor,
                            selectedTextColor = AccentColor,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = DividerColor
                        ),
                        icon = { Icon(Icons.Default.Settings, contentDescription = "الإعدادات") },
                        label = { Text("الإعدادات", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                    )
                }
            },
            containerColor = DarkBackground
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        )
                    )
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        0 -> DownloadTabContent(viewModel, context)
                        1 -> TechExplorerTabContent(viewModel, context)
                        2 -> HistoryTabContent(viewModel, context)
                        3 -> SettingsTabContent(viewModel, context)
                    }
                }
            }
        }
    }
}

// ==========================================
// --- DOWNLOAD TAB CONTENT ---
// ==========================================
@Composable
fun DownloadTabContent(viewModel: XDownloaderViewModel, context: Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Hero Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(AccentColor.copy(alpha = 0.2f), CircleShape)
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Launch,
                                contentDescription = null,
                                tint = AccentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "تحميل فوري وتلقائي ⚡",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "الصق الرابط وسيقوم بالباقي بثوانٍ معدودة.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Search/Download input box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "رابط الفيديو أو التغريدة:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = viewModel.urlInput,
                        onValueChange = { viewModel.onUrlChange(it) },
                        placeholder = { Text("https://x.com/username/status/...", color = TextSecondary, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Link, contentDescription = "link", tint = AccentColor) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor,
                            focusedContainerColor = DarkBackground.copy(alpha = 0.5f),
                            unfocusedContainerColor = DarkBackground.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quality settings selection
                    Text(
                        text = "جودة الفيديو المفضلة:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val qualities = listOf("1080", "720", "480", "360")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        qualities.forEach { q ->
                            val isSelected = viewModel.selectedQuality == q
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) AccentColor else DividerColor,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.selectedQuality = q }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${q}p",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TextPrimary else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mode selection
                    Text(
                        text = "نوع الوسائط المستهدفة:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val modes = listOf("auto" to "فيديو وصوت", "audio" to "صوت فقط (MP3)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        modes.forEach { (m, label) ->
                            val isSelected = viewModel.downloadMode == m
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) AccentColor else DividerColor,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.downloadMode = m }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TextPrimary else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action download button
                    Button(
                        onClick = { viewModel.startDownload(context) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "تحميل")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ابدأ المعالجة والتحميل الآن",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // API processing feedback state panel
        item {
            when (val state = viewModel.downloadState) {
                is DownloadState.Loading -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = AccentColor)
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "جاري الاتصال والتحليل الفوري مع خوادم تفعيل السيرفرات...",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                is DownloadState.Success -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlowColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = GlowColor,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "تم فك التشفير وبدأ التنزيل بنجاح!",
                                    color = GlowColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "الاسم: ${state.mediaTitle}",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.resetState() },
                                    colors = ButtonDefaults.buttonColors(containerColor = DividerColor),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("تطهير الحالة", color = TextPrimary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
                is DownloadState.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Red.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "فشل المعالجة!",
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.message,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.resetState() },
                                colors = ButtonDefaults.buttonColors(containerColor = DividerColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("حسناً", color = TextPrimary, fontSize = 11.sp)
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

// ==========================================
// --- PORTAL TECH EXPLORER TAB CONTENT ---
// ==========================================
@Composable
fun TechExplorerTabContent(viewModel: XDownloaderViewModel, context: Context) {
    val allTechItems by viewModel.allTechItems.collectAsState()
    
    // Advanced Search & Sorting States
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("الكل") }
    var selectedSortCriteria by remember { mutableStateOf("الشعبية") } // الشعبية | الأهمية | تاريخ الإضافة
    var selectedComplexity by remember { mutableStateOf("الكل") } // الكل | مبتدئ | متوسط | خبير
    var selectedYear by remember { mutableStateOf("الكل") } // الكل | 2024 | 2025 | أخرى
    var showOnlyFavorites by remember { mutableStateOf(false) }

    // Dynamic advanced filter section expansion toggler
    var isFiltersExpanded by remember { mutableStateOf(false) }

    // Custom Tech addition dialog triggers
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Addition dialog inputs
    var addTitle by remember { mutableStateOf("") }
    var addCategory by remember { mutableStateOf("الذكاء الاصطناعي") }
    var addDescription by remember { mutableStateOf("") }
    var addYear by remember { mutableStateOf("2026") }
    var addComplexity by remember { mutableStateOf("متوسط") }
    var addImportance by remember { mutableStateOf(5) }
    var addPopularity by remember { mutableStateOf(92) }
    var addLogo by remember { mutableStateOf("auto_awesome") }

    // Filter, search and Sort calculations
    val filteredAndSortedItems = remember(
        allTechItems, searchQuery, selectedCategory, selectedSortCriteria, selectedComplexity, selectedYear, showOnlyFavorites
    ) {
        var list = allTechItems
        
        if (searchQuery.isNotBlank()) {
            list = list.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
            }
        }
        
        if (selectedCategory != "الكل") {
            list = list.filter { it.category == selectedCategory }
        }
        
        if (selectedComplexity != "الكل") {
            list = list.filter { it.complexity == selectedComplexity }
        }
        
        if (selectedYear != "الكل") {
            list = list.filter {
                when (selectedYear) {
                    "2024" -> it.launchYear == 2024
                    "2025" -> it.launchYear == 2025
                    "أخرى" -> it.launchYear != 2024 && it.launchYear != 2025
                    else -> true
                }
            }
        }
        
        if (showOnlyFavorites) {
            list = list.filter { it.isFavorite }
        }
        
        when (selectedSortCriteria) {
            "الشعبية" -> list.sortedByDescending { it.popularity }
            "الأهمية" -> list.sortedByDescending { it.importance }
            "تاريخ الإضافة" -> list.sortedByDescending { it.timestamp }
            else -> list
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // --- 1. FEATURED TECH BENTO BANNER CELL ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AccentColor),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AccentColor, RoundedCornerShape(24.dp))
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.22f), CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "بوابة الابتكار والتقنيات ✨",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(GlowColor, CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "محدث لحظياً",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "مستودع التقنيات الحديثة 🚀",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 28.sp
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "تصفح آخر صيحات الابتكار التقني وقم بترشيح وتصنيف مفضلاتك بكفاءة عالية.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "إضافة لابتكارك", tint = AccentColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إضافة تقنية", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentColor)
                            }
                        }
                    }
                }
            }
        }

        // --- 2. ADVANCED SEARCH & FILTERING CELL ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "صندوق البحث المتقدم والفرز الذكي 🔍",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Keyword text input field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("ابحث بكلمة مفتاحية (مثل: Gemini, Web)...", color = TextSecondary, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث", tint = TextSecondary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "مسح", tint = TextSecondary)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor,
                            focusedContainerColor = DarkBackground.copy(alpha = 0.5f),
                            unfocusedContainerColor = DarkBackground.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // Categories slider / Chips layout
                    Text("اختر الفئة البرمجية والابتكارية:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    val categories = listOf("الكل", "الذكاء الاصطناعي", "تطوير الويب", "الأمن السيبراني", "التقنيات السحابية")
                    Box(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(categories) { cat ->
                                val isSelected = selectedCategory == cat
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isSelected) AccentColor else DividerColor,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedCategory = cat }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TextPrimary else TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Expandable advanced properties box
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isFiltersExpanded = !isFiltersExpanded }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FilterList, contentDescription = "تصفية متقدمة", tint = AccentColor, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "تصفية متقدمة (سنة الإطلاق، الصعوبة...)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentColor
                                )
                            }
                            Icon(
                                imageVector = if (isFiltersExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "عرض التفاصيل",
                                tint = AccentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (isFiltersExpanded) {
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // Year level
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("سنة الإطلاق:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val years = listOf("الكل", "2024", "2025", "أخرى")
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        years.forEach { yr ->
                                            val isYrSelected = selectedYear == yr
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .background(
                                                        if (isYrSelected) AccentColor else DividerColor,
                                                        RoundedCornerShape(6.dp)
                                                    )
                                                    .clickable { selectedYear = yr }
                                                    .padding(vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = yr,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isYrSelected) TextPrimary else TextSecondary
                                                )
                                            }
                                        }
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("مستوى التعقيد:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val levels = listOf("الكل", "مبتدئ", "متوسط", "خبير")
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        levels.forEach { lv ->
                                            val isLvSelected = selectedComplexity == lv
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .background(
                                                        if (isLvSelected) AccentColor else DividerColor,
                                                        RoundedCornerShape(6.dp)
                                                    )
                                                    .clickable { selectedComplexity = lv }
                                                    .padding(vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = lv,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isLvSelected) TextPrimary else TextSecondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. SORTING CRITERIA & FAVORITES ACTION TABS BAR ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sorting Buttons Layout
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    listOf("الشعبية", "الأهمية", "تاريخ الإضافة").forEach { crit ->
                        val isCritSel = selectedSortCriteria == crit
                        val chipLabel = when (crit) {
                            "الشعبية" -> "الأكثر شعبية 🔥"
                            "الأهمية" -> "الأعلى أهمية ⭐"
                            else -> "الأحدث إضافة 📅"
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isCritSel) AccentColor else SurfaceCard,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
                                .clickable { selectedSortCriteria = crit }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = chipLabel,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Favorites switch button
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (showOnlyFavorites) AccentColor else SurfaceCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
                        .clickable { showOnlyFavorites = !showOnlyFavorites }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "مفضلتي",
                            tint = if (showOnlyFavorites) Color.Red else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "المفضلة",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // --- 4. LIST COUNTERS & DATA ---
        if (filteredAndSortedItems.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
                        .padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = TextSecondary.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "لم نجد أي تقنيات مطابقة للمواصفات!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "جرب تعديل خيارات البحث والكلمات الدليلية، أو أضف تقنيتك الفريدة!",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        } else {
            items(filteredAndSortedItems) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val logoIcon = when (item.logoName) {
                                    "security" -> Icons.Default.Security
                                    "web" -> Icons.Default.Language
                                    "cloud" -> Icons.Default.Cloud
                                    "code" -> Icons.Default.Code
                                    "terminal" -> Icons.Default.Terminal
                                    "dns" -> Icons.Default.Dns
                                    "wifi" -> Icons.Default.Wifi
                                    else -> Icons.Default.AutoAwesome
                                }
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(AccentColor.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = logoIcon,
                                        contentDescription = null,
                                        tint = AccentColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = item.category,
                                        fontSize = 11.sp,
                                        color = AccentColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val isCustom = item.id > 10
                                if (isCustom) {
                                    IconButton(
                                        onClick = {
                                            viewModel.deleteTechItem(item.id)
                                            Toast.makeText(context, "تمت إزالة التقنية بنجاح", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = Color.Red.copy(alpha = 0.8f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { viewModel.toggleFavorite(item.id, item.isFavorite) }
                                ) {
                                    Icon(
                                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "المفضلة",
                                        tint = if (item.isFavorite) Color.Red else TextSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.description,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(DividerColor, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "إطلاق: ${item.launchYear}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }

                                val (bgCol, txtCol) = when (item.complexity) {
                                    "خبير" -> Color.Red.copy(alpha = 0.2f) to Color.Red
                                    "متوسط" -> Color(0xFFFFF3E0).copy(alpha = 0.2f) to Color(0xFFFF9800)
                                    else -> Color.Green.copy(alpha = 0.2f) to Color.Green
                                }
                                Box(
                                    modifier = Modifier
                                        .background(bgCol, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "المستوى: ${item.complexity}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = txtCol
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                repeat(5) { starIndex ->
                                    Icon(
                                        imageVector = if (starIndex < item.importance) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = if (starIndex < item.importance) GoldColor else TextSecondary.copy(alpha = 0.4f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "معدل الشعبية:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            LinearProgressIndicator(
                                progress = { item.popularity / 100f },
                                trackColor = DividerColor,
                                color = AccentColor,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = "${item.popularity}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentColor
                            )
                        }
                    }
                }
            }
        }
    }

    // --- CUSTOM TECH ADDITION DIALOG ---
    if (showAddDialog) {
        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
            ) {
                LazyColumn(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "أضف ابتكارك أو تقنية متميزة 💡",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            IconButton(onClick = { showAddDialog = false }) {
                                Icon(Icons.Default.Close, contentDescription = "أغلق", tint = TextSecondary)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    item {
                        Text("اسم الابتكار التقني:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = addTitle,
                            onValueChange = { addTitle = it },
                            placeholder = { Text("مثال: ريأكت نيتف، فلاتر...", fontSize = 12.sp, color = TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AccentColor,
                                unfocusedBorderColor = DividerColor
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    item {
                        Text("اختر الفئة البرمجية الصالحة:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        val options = listOf("الذكاء الاصطناعي", "تطوير الويب", "الأمن السيبراني", "التقنيات السحابية")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            options.forEach { op ->
                                val active = addCategory == op
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (active) AccentColor else DividerColor,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { addCategory = op }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = op,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text("وصف للتقنية ومزاياها البارزة:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = addDescription,
                            onValueChange = { addDescription = it },
                            placeholder = { Text("اكتب نبذة مختصرة تلخص أهمية التقنية وتأثيرها...", fontSize = 12.sp, color = TextSecondary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AccentColor,
                                unfocusedBorderColor = DividerColor
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("سنة الإطلاق:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = addYear,
                                    onValueChange = { addYear = it },
                                    placeholder = { Text("مثال: 2026", fontSize = 12.sp, color = TextSecondary) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        focusedBorderColor = AccentColor,
                                        unfocusedBorderColor = DividerColor
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("المستوى الفني:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                val levels = listOf("مبتدئ", "متوسط", "خبير")
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    levels.forEach { lv ->
                                        val active = addComplexity == lv
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(
                                                    if (active) AccentColor else DividerColor,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable { addComplexity = lv }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = lv,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text("مستوى الأهمية:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { ind ->
                                val starVal = ind + 1
                                val isActive = addImportance >= starVal
                                IconButton(onClick = { addImportance = starVal }) {
                                    Icon(
                                        imageVector = if (isActive) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = if (isActive) GoldColor else TextSecondary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text("معدل الرواج والشعبية الحالي (${addPopularity}%):", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Slider(
                            value = addPopularity.toFloat(),
                            onValueChange = { addPopularity = it.toInt() },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(activeTrackColor = AccentColor, thumbColor = AccentColor)
                        )
                    }

                    item {
                        Text("اختر رمز اللوغو للتقنية:", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        val iconKeys = listOf("auto_awesome", "security", "web", "cloud", "code")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            iconKeys.forEach { key ->
                                val active = addLogo == key
                                val logIc = when (key) {
                                    "security" -> Icons.Default.Security
                                    "web" -> Icons.Default.Language
                                    "cloud" -> Icons.Default.Cloud
                                    "code" -> Icons.Default.Code
                                    else -> Icons.Default.AutoAwesome
                                }
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (active) AccentColor else DividerColor,
                                            CircleShape
                                        )
                                        .clickable { addLogo = key }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = logIc,
                                        contentDescription = null,
                                        tint = TextPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (addTitle.isBlank() || addDescription.isBlank()) {
                                    Toast.makeText(context, "الرجاء كمالة الحقول الأساسية!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.addNewTech(
                                    title = addTitle,
                                    category = addCategory,
                                    description = addDescription,
                                    launchYear = addYear.toIntOrNull() ?: 2026,
                                    complexity = addComplexity,
                                    importance = addImportance,
                                    popularity = addPopularity,
                                    logoName = addLogo
                                )
                                Toast.makeText(context, "تمت إضافة تقنيتك الجديدة بنجاح!", Toast.LENGTH_SHORT).show()
                                showAddDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("أحفظ وانشر الابتكار برمجياً", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- DOWNLOAD HISTORY TAB CONTENT ---
// ==========================================
@Composable
fun HistoryTabContent(viewModel: XDownloaderViewModel, context: Context) {
    val downloads by viewModel.allDownloads.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "سجل التنزيلات للوسائط 📂",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            if (downloads.isNotEmpty()) {
                TextButton(onClick = { viewModel.clearAllDownloads() }) {
                    Text("مسح السجل بالكامل", color = Color.Red, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (downloads.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "السجل فارغ تماماً!",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "لم تقم بتنزيل أي مقاطع فيديو أو ملفات بعد.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(downloads) { download ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DividerColor, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(AccentColor.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (download.mediaType == "صوت") Icons.Default.MusicNote else Icons.Default.VideoLibrary,
                                    contentDescription = null,
                                    tint = AccentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = download.title,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "النوع: ${download.mediaType} | الجودة: ${download.quality ?: "تلقائي"}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            IconButton(onClick = { viewModel.deleteDownload(download.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "حذف المادة",
                                    tint = Color.Red.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- SETTINGS TAB CONTENT ---
// ==========================================
@Composable
fun SettingsTabContent(viewModel: XDownloaderViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "إعدادات التطبيق والمطورين 🛠️",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DividerColor, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "معلومات الاتصال وخوادم التحميل:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "يستخدم هذا التطبيق واجهة برمجة تطبيقات Cobalt API المفتوحة لحل روابط منصات التواصل الاجتماعي وفك تشفير جودة الفيديو والصورة بطرق أمنة ومحمية.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DividerColor, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "إعدادات حفظ الملفات:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "يتم تنزيل جميع الملفات والوسائط مباشرة إلى مجلد التنزيلات (Downloads) الرئيسي بهاتفك، لتتمكن من الوصول إليها في أي وقت دون الحاجة لفتح التطبيق.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DividerColor, RoundedCornerShape(20.dp))
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
                        text = "نسخة التطبيق الحالية:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "اكس عربي v1.0 (إطلاق مستقر نهائي)",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .background(AccentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("نهائي", color = AccentColor, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
