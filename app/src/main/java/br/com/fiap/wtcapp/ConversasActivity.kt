package br.com.fiap.wtcapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.ui.theme.WTCTheme

class ConversasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ConversasScreen()
                }
            }
        }
    }
}

data class Chat(
    val nome: String,
    val tipo: String, // "1:1" ou "Grupo"
    val ultimaMensagem: String
)

@Composable
fun ConversasScreen() {
    val chats = listOf(
        Chat("Ana Souza", "1:1", "Olá! Preciso de ajuda com meu pedido."),
        Chat("Equipe Vendas", "Grupo", "Relatório enviado no grupo."),
        Chat("Carlos Lima", "1:1", "Recebi a campanha, obrigado!")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Conversas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chats) { chat ->
                ChatCard(chat)
            }
        }

        Button(
            onClick = { /* abrir nova conversa */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text("Nova Conversa", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ChatCard(chat: Chat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(chat.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text("Tipo: ${chat.tipo}", fontSize = 12.sp, color = Color(0xFF999999))
            Text(chat.ultimaMensagem, fontSize = 14.sp, color = Color(0xFF555555))
        }
    }
}