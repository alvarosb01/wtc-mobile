package br.com.fiap.wtcapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.ui.theme.WTCTheme

class MensagensActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MensagensScreen()
                }
            }
        }
    }
}

data class Mensagem(
    val titulo: String,
    val conteudo: String,
    val link: String?,
    val nova: Boolean
)

@Composable
fun MensagensScreen() {
    val context = LocalContext.current
    var busca by remember { mutableStateOf("") }

    val mensagens = listOf(
        Mensagem("Promoção exclusiva", "Aproveite 30% de desconto até hoje!", "https://promo.wtc.com", true),
        Mensagem("Seu boleto está disponível", "Clique abaixo para visualizar o boleto do mês.", "https://boleto.wtc.com", false),
        Mensagem("Obrigado pela compra", "Sua avaliação é muito importante para nós.", null, false)
    )

    val filtradas = mensagens.filter {
        busca.isBlank() || it.titulo.contains(busca, ignoreCase = true) || it.conteudo.contains(busca, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Mensagens",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = busca,
            onValueChange = { busca = it },
            label = { Text("Buscar mensagem") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtradas) { msg ->
                MensagemCard(msg, onLinkClick = {
                    msg.link?.let { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                })
            }
        }
    }
}

@Composable
fun MensagemCard(msg: Mensagem, onLinkClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (msg.nova) Color(0xFFE3F2FD) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(msg.titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text(msg.conteudo, fontSize = 14.sp, color = Color(0xFF555555))
            if (msg.link != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Abrir link",
                    fontSize = 14.sp,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.clickable { onLinkClick() }
                )
            }
        }
    }
}