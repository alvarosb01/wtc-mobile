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
import androidx.compose.foundation.layout.FlowRow

class ContatosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ContatosScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContatosScreen() {
    var busca by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf("Todos") }

    val contatos = listOf(
        Cliente("Ana Souza", "ana@email.com", "Ativa", listOf("VIP"), 92),
        Cliente("Carlos Lima", "carlos@email.com", "Inativo", listOf("Recente"), 65),
        Cliente("Fernanda Rocha", "fernanda@email.com", "Ativa", listOf("Fidelidade"), 88)
    )

    val contatosFiltrados = contatos.filter {
        (filtroSelecionado == "Todos" || it.status == filtroSelecionado || it.tags.contains(filtroSelecionado)) &&
                (busca.isBlank() || it.nome.contains(busca, ignoreCase = true) || it.email.contains(busca, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Contatos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 🔍 Campo de busca
        OutlinedTextField(
            value = busca,
            onValueChange = { busca = it },
            label = { Text("Buscar por nome ou e-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🏷️ Filtros centralizados com quebra automática
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // antes era 8.dp
        ) {
            listOf("Todos", "VIP", "Fidelidade", "Ativa", "Inativo").forEach { filtro ->
                FilterChip(
                    label = filtro,
                    selected = filtroSelecionado == filtro,
                    onClick = { filtroSelecionado = filtro }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📋 Lista com scroll
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contatosFiltrados) { cliente ->
                ContactCard(cliente)
            }
        }

        // ➕ Botão de adicionar contato
        Button(
            onClick = { /* ação futura */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text("Adicionar Contato", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        shape = RoundedCornerShape(50),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) Color(0xFF1976D2) else Color(0xFFE3F2FD),
            labelColor = if (selected) Color.White else Color(0xFF1976D2)
        )
    )
}

data class Cliente(
    val nome: String,
    val email: String,
    val status: String,
    val tags: List<String>,
    val score: Int
)

@Composable
fun ContactCard(cliente: Cliente) {
    var anotacao by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(cliente.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text(cliente.email, fontSize = 14.sp, color = Color(0xFF555555))
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Status: ${cliente.status}", fontSize = 12.sp, color = Color(0xFF999999))
                Text("Score: ${cliente.score}", fontSize = 12.sp, color = Color(0xFF999999))
            }
            Text("Tags: ${cliente.tags.joinToString()}", fontSize = 12.sp, color = Color(0xFF999999))

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = anotacao,
                onValueChange = { anotacao = it },
                label = { Text("Anotação rápida") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
    }
}