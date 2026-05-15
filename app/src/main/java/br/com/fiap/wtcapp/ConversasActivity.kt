package br.com.fiap.wtcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import br.com.fiap.wtcapp.model.CustomerResponse
import br.com.fiap.wtcapp.network.RetrofitClient
import br.com.fiap.wtcapp.ui.theme.WTCTheme
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversasScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var listaEmpresas by remember { mutableStateOf(listOf<CustomerResponse>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val service = RetrofitClient.getCustomerService(context)
                val response = service.listCustomers(page = 0, size = 50)
                if (response.isSuccessful) {
                    listaEmpresas = response.body()?.content ?: listOf()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao carregar canais", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Canais de Atendimento", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color.White)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(listaEmpresas) { empresa ->
                        CardConversa(empresa) {
                            // AO CLICAR, ABRE O CHAT PASSANDO O ID E NOME
                            val intent = Intent(context, MensagensActivity::class.java)
                            intent.putExtra("CLIENTE_ID", empresa.id)
                            intent.putExtra("CLIENTE_NOME", empresa.name)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardConversa(empresa: CustomerResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFF1976D2), CircleShape), contentAlignment = Alignment.Center) {
                Text(empresa.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = empresa.name, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Text(text = "Clique para abrir o canal de suporte", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}