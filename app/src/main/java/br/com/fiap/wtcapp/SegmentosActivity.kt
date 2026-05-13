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

class SegmentosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SegmentosScreen()
                }
            }
        }
    }
}

data class Segmento(
    val nome: String,
    val descricao: String,
    val totalClientes: Int
)

@Composable
fun SegmentosScreen() {
    var busca by remember { mutableStateOf("") }

    val segmentos = listOf(
        Segmento("VIP", "Clientes com alto valor e fidelidade", 18),
        Segmento("Recente", "Novos clientes cadastrados", 42),
        Segmento("Score > 80", "Clientes com alta pontuação", 27),
        Segmento("Inativos", "Clientes sem interação recente", 12)
    )

    val filtrados = segmentos.filter {
        busca.isBlank() || it.nome.contains(busca, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Segmentos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = busca,
            onValueChange = { busca = it },
            label = { Text("Buscar segmento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtrados) { segmento ->
                SegmentoCard(segmento)
            }
        }

        Button(
            onClick = { /* ação futura */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text("Criar Novo Segmento", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SegmentoCard(segmento: Segmento) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(segmento.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text(segmento.descricao, fontSize = 14.sp, color = Color(0xFF555555))
            Text("Clientes: ${segmento.totalClientes}", fontSize = 12.sp, color = Color(0xFF999999))
        }
    }
}