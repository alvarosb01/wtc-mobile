package br.com.fiap.wtcapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    val role = tokenManager.getRole() ?: "CLIENTE"
    val meuId = tokenManager.getUserId() ?: ""
    val isOperador = role.equals("OPERADOR", ignoreCase = true)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 🔷 Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("WTC Business", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                    Text("Perfil: $role", fontSize = 12.sp, color = Color.Gray)
                }
                IconButton(onClick = {
                    tokenManager.logout()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "Sair", tint = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔷 OPÇÕES DE MENU

            // 1. CANAIS DE ATENDIMENTO (Para todos, mas com lógica diferente)
            MenuOption(
                title = "Canais de Atendimento",
                description = "Comunicação estratégica 1:1",
                emoji = "💬",
                onClick = {
                    if (isOperador) {
                        context.startActivity(Intent(context, ConversasActivity::class.java))
                    } else {
                        val intent = Intent(context, MensagensActivity::class.java)
                        intent.putExtra("CLIENTE_ID", meuId)
                        intent.putExtra("CLIENTE_NOME", "Suporte WTC")
                        context.startActivity(intent)
                    }
                }
            )

            // 2. GESTÃO DE CLIENTES (Só Operador vê)
            if (isOperador) {
                Spacer(modifier = Modifier.height(16.dp))
                MenuOption(
                    title = "Gestão de Clientes",
                    description = "Carteira de empresas e prospecção",
                    emoji = "🏢",
                    onClick = { context.startActivity(Intent(context, ContatosActivity::class.java)) }
                )

                // 3. AÇÕES & CAMPANHAS
                Spacer(modifier = Modifier.height(16.dp))
                MenuOption(
                    title = "Ações & Campanhas",
                    description = "Disparos estratégicos e métricas",
                    emoji = "🚀",
                    onClick = { context.startActivity(Intent(context, CampanhasActivity::class.java)) }
                )

                // 4. INTELIGÊNCIA DE MERCADO
                Spacer(modifier = Modifier.height(16.dp))
                MenuOption(
                    title = "Inteligência de Mercado",
                    description = "Segmentação e score de clientes",
                    emoji = "📊",
                    onClick = { context.startActivity(Intent(context, SegmentosActivity::class.java)) }
                )
            }

            // 5. CENTRAL DE NOTIFICAÇÕES (Para todos)
            Spacer(modifier = Modifier.height(16.dp))
            MenuOption(
                title = "Central de Notificações",
                description = "Alertas e interações recentes",
                emoji = "🔔",
                onClick = {
                    val intent = Intent(context, MensagensActivity::class.java)
                    intent.putExtra("CLIENTE_ID", meuId)
                    intent.putExtra("CLIENTE_NOME", "Notificações")
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun MenuOption(title: String, description: String, emoji: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(Color(0xFF1976D2), CircleShape), contentAlignment = Alignment.Center) {
                Text(emoji, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}