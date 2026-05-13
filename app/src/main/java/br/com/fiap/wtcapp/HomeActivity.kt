package br.com.fiap.wtcapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.ui.theme.WTCTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // 🔷 Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WTC",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )

                IconButton(onClick = { /* abrir configurações futuramente */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configurações",
                        tint = Color(0xFF1976D2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bem-vindo! Gerencie sua comunicação com clientes de forma inteligente.",
                fontSize = 14.sp,
                color = Color(0xFF555555)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔷 Menu interativo com navegação
            Column(modifier = Modifier.fillMaxWidth()) {
                MenuOption(
                    title = "Contatos",
                    description = "CRM com busca, filtros e anotações",
                    emoji = "📇",
                    onClick = {
                        context.startActivity(Intent(context, ContatosActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Conversas",
                    description = "Chat 1:1 e por segmento",
                    emoji = "💬 🔴",
                    onClick = {
                        context.startActivity(Intent(context, ConversasActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Campanhas",
                    description = "Envios rápidos e segmentados",
                    emoji = "🚀",
                    onClick = {
                        context.startActivity(Intent(context, CampanhasActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Segmentos",
                    description = "Agrupamentos por tag, score e status",
                    emoji = "🧩",
                    onClick = {
                        context.startActivity(Intent(context, SegmentosActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Mensagens",
                    description = "Receba e interaja com campanhas",
                    emoji = "📥",
                    onClick = {
                        context.startActivity(Intent(context, MensagensActivity::class.java))
                    }
                )
            }
        }

        // 🔻 Rodapé fixo
        Text(
            text = "© 2025 WTC. Todos os direitos reservados.",
            fontSize = 12.sp,
            color = Color(0xFF999999),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
fun MenuOption(title: String, description: String, emoji: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF1976D2), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color(0xFF555555)
                )
            }
        }
    }
}