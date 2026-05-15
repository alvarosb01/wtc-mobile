package br.com.fiap.wtcapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.model.MessageResponse
import br.com.fiap.wtcapp.model.SendMessageRequest
import br.com.fiap.wtcapp.network.RetrofitClient
import br.com.fiap.wtcapp.network.TokenManager
import br.com.fiap.wtcapp.ui.theme.WTCTheme
import kotlinx.coroutines.launch

class MensagensActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dados vindos da tela anterior (usado pelo Operador)
        val clienteIdDaLista = intent.getStringExtra("CLIENTE_ID") ?: ""
        val clienteNomeDaLista = intent.getStringExtra("CLIENTE_NOME") ?: "Central de Atendimento"

        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MensagensScreen(clienteIdDaLista, clienteNomeDaLista)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensagensScreen(clienteIdDaLista: String, clienteNomeDaLista: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val tokenManager = TokenManager(context)

    // Identificação do Usuário Logado
    val minhaRole = tokenManager.getRole() ?: "CLIENTE"
    val meuId = tokenManager.getUserId() ?: ""

    // LÓGICA DE ID: Se for Operador, usa quem ele clicou. Se for Cliente, usa o próprio ID dele.
    val idConversaReal = if (minhaRole.equals("OPERADOR", ignoreCase = true)) clienteIdDaLista else meuId
    val tituloTela = if (minhaRole.equals("OPERADOR", ignoreCase = true)) clienteNomeDaLista else "Suporte WTC"

    var listaMensagens by remember { mutableStateOf(listOf<MessageResponse>()) }
    var textoMensagem by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    fun carregarMensagens() {
        scope.launch {
            try {
                val service = RetrofitClient.getMessageService(context)
                val response = service.getHistorico(idConversaReal)
                if (response.isSuccessful) {
                    listaMensagens = response.body() ?: listOf()
                    if (listaMensagens.isNotEmpty()) {
                        listState.animateScrollToItem(listaMensagens.size - 1)
                    }
                }
            } catch (e: Exception) {
                Log.e("WTC_ERROR", "Falha ao carregar: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        carregarMensagens()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloTela, color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2))
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp, modifier = Modifier.imePadding()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textoMensagem,
                        onValueChange = { textoMensagem = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Digite sua mensagem...") },
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (textoMensagem.isNotBlank()) {
                                scope.launch {
                                    try {
                                        val request = SendMessageRequest(
                                            targetType = "CUSTOMER",
                                            subject = "Chat via App",
                                            content = textoMensagem,
                                            customerId = idConversaReal,
                                            conversationId = idConversaReal
                                        )

                                        val response = RetrofitClient.getMessageService(context).enviarMensagem(request)

                                        if (response.isSuccessful) {
                                            textoMensagem = ""
                                            carregarMensagens()
                                        } else {
                                            Toast.makeText(context, "Erro no servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Falha de rede", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color(0xFF1976D2))
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFECE5DD))) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (listaMensagens.isEmpty()) {
                Text("Nenhuma mensagem encontrada", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(listaMensagens) { msg ->
                        BubbleMensagem(msg)
                    }
                }
            }
        }
    }
}

@Composable
fun BubbleMensagem(msg: MessageResponse) {
    val isMe = msg.senderRole?.equals("OPERADOR", ignoreCase = true) == true

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = if (isMe) Color(0xFFDCF8C6) else Color.White),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = msg.content ?: "", fontSize = 15.sp, color = Color.Black)
                Text(
                    text = if (isMe) "WTC Operador" else "Cliente",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}