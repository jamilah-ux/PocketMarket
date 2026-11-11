package com.example.pocketmarket.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentLanguage: String = "ar",
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEn = currentLanguage == "en"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEn) "My Account & Orders" else "حسابي والطلبات") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // كارد معلومات المستخدم
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "J",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Jamila Jalal",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "jamila@example.com",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "+967 4567 123 50",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout"
                            )
                        }
                    }
                }
            }

            // عنوان الطلبات السابقة
            item {
                Text(
                    text = if (isEn) "My Previous Orders (3)" else "طلباتي السابقة (3)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // قائمة الطلبات الوهمية
            val orders = listOf(
                Triple("982341#", if (isEn) "Pending" else "قيد المعالجة", "\$1752.16"),
                Triple("871203#", if (isEn) "Delivered" else "تم التوصيل", "\$320.00"),
                Triple("749102#", if (isEn) "Delivered" else "تم التوصيل", "\$85.50")
            )

            items(orders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${if (isEn) "Order #" else "طلب رقم "}${order.first}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (order.second.contains("Pending") || order.second.contains("قيد")) Icons.Default.Schedule else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (order.second.contains("Pending") || order.second.contains("قيد")) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = order.second,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${if (isEn) "Total: " else "المبلغ الإجمالي: "}${order.third}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}