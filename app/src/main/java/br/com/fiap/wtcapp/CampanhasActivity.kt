package br.com.fiap.wtcapp

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.ui.theme.WTCTheme

class CampanhasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CampanhasScreen()
                }
            }
        }
    }
}

data class Campanha(
    val titulo: String,
    val status: String,
    val data: String
)

@Composable
fun CampanhasScreen() {
    val context = LocalContext.current

    val campanhas = listOf(
        Campanha("Black Friday", "Enviada", "24/10/2025"),
        Campanha("Novidades Outubro", "Agendada", "28/10/2025"),
        Campanha("Pesquisa de Satisfação", "Rascunho", "Em edição")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Campanhas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 🔷 Métricas visuais
        MetricGrid()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Campanhas Recentes",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(campanhas) { campanha ->
                CampanhaCard(campanha)
            }
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, CriarCampanhaActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text("Criar Nova Campanha", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricGrid() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricCard("📨", "Enviadas", "12")
            MetricCard("📬", "Abertura", "68%")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricCard("🔗", "Cliques", "34%")
            MetricCard("📥", "Respostas", "5%")
        }
    }
}

@Composable
fun MetricCard(emoji: String, label: String, value: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Text(label, fontSize = 13.sp, color = Color(0xFF555555))
            }
        }
    }
}

@Composable
fun CampanhaCard(campanha: Campanha) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(campanha.titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text("Status: ${campanha.status}", fontSize = 14.sp, color = Color(0xFF555555))
            Text("Data: ${campanha.data}", fontSize = 12.sp, color = Color(0xFF999999))
        }
    }
}