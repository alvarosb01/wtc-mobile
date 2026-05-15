package br.com.fiap.wtcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import br.com.fiap.wtcapp.model.CustomerResponse
import br.com.fiap.wtcapp.network.RetrofitClient
import br.com.fiap.wtcapp.network.TokenManager
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 1. Instancia o TokenManager para ler a Role salva no Login
    val tokenManager = TokenManager(context)
    val roleUsuario = tokenManager.getRole() ?: "CLIENTE"

    // Estados da UI
    var busca by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf("Todos") }

    // Estados de Dados (Vindo da API)
    var listaClientes by remember { mutableStateOf(listOf<CustomerResponse>()) }
    var isLoading by remember { mutableStateOf(true) }

    // --- BUSCA OS DADOS NO SPRING ---
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val service = RetrofitClient.getCustomerService(context)
                val response = service.listCustomers(page = 0, size = 50)

                if (response.isSuccessful) {
                    listaClientes = response.body()?.content ?: listOf()
                } else {
                    Toast.makeText(context, "Erro ao carregar empresas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Falha na conexão: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    // --- LÓGICA DE FILTRO LOCAL ---
    val clientesFiltrados = listaClientes.filter { cliente ->
        val atendeFiltro = when (filtroSelecionado) {
            "VIP" -> cliente.vip
            "Fidelidade" -> cliente.fidelidade
            "Ativa" -> cliente.ativo
            "Inativo" -> !cliente.ativo
            else -> true // "Todos"
        }
        val atendeBusca = cliente.name.contains(busca, ignoreCase = true) ||
                cliente.document.contains(busca, ignoreCase = true)

        atendeFiltro && atendeBusca
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Clientes (WTC)",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = busca,
            onValueChange = { busca = it },
            label = { Text("Buscar empresa ou documento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
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

        if (isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1976D2))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clientesFiltrados) { cliente ->
                    ContactCard(cliente)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TRAVA DE SEGURANÇA VISUAL (RBAC) ---
        // Só renderiza o botão se a role for OPERADOR (independente de maiúscula/minúscula)
        if (roleUsuario.equals("OPERADOR", ignoreCase = true)) {
            Button(
                onClick = {
                    val intent = Intent(context, CadastroClienteActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Text("Adicionar Nova Empresa", color = Color.White, fontWeight = FontWeight.Bold)
            }
        } else {
            // Se for CLIENTE, mostra apenas um texto informativo opcional
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Acesso de consulta: Perfil $roleUsuario",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
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

@Composable
fun ContactCard(cliente: CustomerResponse) {
    var anotacao by remember { mutableStateOf("") }

    val tags = mutableListOf<String>()
    if (cliente.vip) tags.add("VIP")
    if (cliente.fidelidade) tags.add("Fidelidade")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(cliente.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text("Doc: ${cliente.document}", fontSize = 14.sp, color = Color(0xFF555555))

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = if (cliente.ativo) "Status: Ativa" else "Status: Inativa",
                    fontSize = 12.sp,
                    color = if (cliente.ativo) Color(0xFF4CAF50) else Color.Red
                )
            }

            if (tags.isNotEmpty()) {
                Text("Tags: ${tags.joinToString()}", fontSize = 12.sp, color = Color(0xFF999999))
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = anotacao,
                onValueChange = { anotacao = it },
                label = { Text("Nota comercial interna") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
    }
}