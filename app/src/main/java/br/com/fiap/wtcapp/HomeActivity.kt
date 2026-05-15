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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.network.TokenManager
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
    val tokenManager = TokenManager(context)

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
            // 🔷 Cabeçalho com Nome do Hub e Logout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WTC Business",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = "São Paulo Smart Hub",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2).copy(alpha = 0.7f)
                    )
                }

                // BOTÃO DE LOGOUT (FUNDAMENTAL PARA TROCA DE PERFIL)
                IconButton(onClick = {
                    tokenManager.logout() // Limpa Token e Role
                    val intent = Intent(context, LoginActivity::class.java)
                    // Limpa a pilha de telas para segurança
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0xFFD32F2F) // Vermelho para indicar saída
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bem-vindo ao hub estratégico. Otimize sua prospecção e gestão de relacionamento corporativo.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔷 Menu de Operações B2B
            Column(modifier = Modifier.fillMaxWidth()) {
                MenuOption(
                    title = "Gestão de Clientes",
                    description = "Visualização de carteira e prospecção ativa",
                    emoji = "🏢",
                    onClick = {
                        context.startActivity(Intent(context, ContatosActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Canais de Atendimento",
                    description = "Comunicação direta 1:1 e suporte",
                    emoji = "💬",
                    onClick = {
                        context.startActivity(Intent(context, ConversasActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Ações & Campanhas",
                    description = "Disparos estratégicos e métricas de envio",
                    emoji = "🚀",
                    onClick = {
                        context.startActivity(Intent(context, CampanhasActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Inteligência de Mercado",
                    description = "Segmentação por tags, score e setores",
                    emoji = "📊",
                    onClick = {
                        context.startActivity(Intent(context, SegmentosActivity::class.java))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MenuOption(
                    title = "Central de Notificações",
                    description = "Monitoramento de interações e alertas",
                    emoji = "🔔",
                    onClick = {
                        context.startActivity(Intent(context, MensagensActivity::class.java))
                    }
                )
            }
        }

        // 🔻 Rodapé institucional
        Text(
            text = "© 2025 WTC São Paulo Business Hub",
            fontSize = 11.sp,
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Text(text = emoji, fontSize = 22.sp)
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
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}