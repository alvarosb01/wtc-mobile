package br.com.fiap.wtcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

class CriarCampanhaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CriarCampanhaScreen()
                }
            }
        }
    }
}

@Composable
fun CriarCampanhaScreen() {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    var segmentoSelecionado by remember { mutableStateOf("") }

    val segmentos = listOf("VIP", "Score > 80", "Recente", "Inativos")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Nova Campanha",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título da campanha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = mensagem,
            onValueChange = { mensagem = it },
            label = { Text("Mensagem") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Segmento", fontWeight = FontWeight.SemiBold, color = Color(0xFF555555))
        Spacer(modifier = Modifier.height(8.dp))

        segmentos.forEach { segmento ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { segmentoSelecionado = segmento }
            ) {
                RadioButton(
                    selected = segmentoSelecionado == segmento,
                    onClick = { segmentoSelecionado = segmento }
                )
                Text(text = segmento, fontSize = 14.sp, color = Color(0xFF333333))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (titulo.isBlank() || mensagem.isBlank() || segmentoSelecionado.isBlank()) {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Campanha enviada!", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, CampanhasActivity::class.java))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text("Enviar Campanha", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}