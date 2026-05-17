package br.com.fiap.wtcapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.ui.theme.WTCTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WelcomeScreen()
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    val context = LocalContext.current
    // Estado necessário para permitir rolagem na tela
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                // Habilita o scroll vertical para evitar que o botão suma em telas menores
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 🔷 Branding Section (Identidade Visual)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_wtc),
                    contentDescription = "Logo WTC",
                    modifier = Modifier.size(140.dp) // Tamanho ajustado para caber melhor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "WORLD TRADE CENTER",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1976D2),
                    letterSpacing = 2.sp
                )
                Text(
                    text = "SÃO PAULO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    letterSpacing = 4.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔷 Proposta de Valor
            Text(
                text = "Transforme dados em relacionamentos.",
                fontSize = 15.sp,
                color = Color(0xFF444444),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔷 Cards Estratégicos (Diferenciais)
            Column(modifier = Modifier.fillMaxWidth()) {
                InfoCard(text = "🌐 Conectividade Global – Comunicação corporativa.")
                Spacer(modifier = Modifier.height(8.dp))
                InfoCard(text = "📈 Inteligência Comercial – CRM focado em resultados.")
                Spacer(modifier = Modifier.height(8.dp))
                InfoCard(text = "🎯 Alcance Segmentado – Campanhas para decisores.")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔷 Seção de Ação (Login)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text(
                        text = "ACESSAR PLATAFORMA",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Gestão exclusiva para Clientes e Operadores WTC",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔷 Rodapé Institucional
            Text(
                text = "© 2026 WTC São Paulo Business Hub",
                fontSize = 11.sp,
                color = Color(0xFFBBBBBB),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun InfoCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
        }
    }
}