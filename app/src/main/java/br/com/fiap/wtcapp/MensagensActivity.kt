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
        val idIntent = intent.getStringExtra("CLIENTE_ID") ?: ""
        val nomeIntent = intent.getStringExtra("CLIENTE_NOME") ?: "Conversa"
        setContent { WTCTheme { Surface { MensagensScreen(idIntent, nomeIntent) } } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensagensScreen(idVindoDaIntent: String, nomeVindoDaIntent: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val tokenManager = TokenManager(context)

    val minhaRole = tokenManager.getRole() ?: "CLIENTE"
    val meuId = tokenManager.getUserId() ?: ""

    // Define qual ID usar para o GET e POST
    val idConversaReal = if (minhaRole.equals("OPERADOR", ignoreCase = true)) idVindoDaIntent else meuId

    var listaMensagens by remember { mutableStateOf(listOf<MessageResponse>()) }
    var textoMensagem by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    fun carregar() {
        if (idConversaReal.isBlank()) {
            isLoading = false
            return
        }
        scope.launch {
            try {
                val service = RetrofitClient.getMessageService(context)
                val response = service.getHistorico(idConversaReal)
                if (response.isSuccessful) {
                    val mgs = response.body() ?: listOf()
                    listaMensagens = mgs
                    Log.d("WTC_DEBUG", "Mensagens carregadas: ${mgs.size}")
                    if (listaMensagens.isNotEmpty()) {
                        listState.animateScrollToItem(listaMensagens.size - 1)
                    }
                }
            } catch (e: Exception) {
                Log.e("WTC_DEBUG", "Erro ao carregar: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { carregar() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nomeVindoDaIntent, color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2))
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp, modifier = Modifier.imePadding()) {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = textoMensagem,
                        onValueChange = { textoMensagem = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Mensagem...") },
                        shape = RoundedCornerShape(24.dp)
                    )
                    IconButton(onClick = {
                        if (textoMensagem.isNotBlank() && idConversaReal.isNotBlank()) {
                            scope.launch {
                                try {
                                    // PREENCHENDO TODOS OS 8 CAMPOS DO SEU RECORD JAVA
                                    val req = SendMessageRequest(
                                        targetType = "CUSTOMER",
                                        subject = "Chat WTC",
                                        content = textoMensagem,
                                        customerId = idConversaReal,
                                        segmentId = null,
                                        groupName = null,
                                        customerIds = null,
                                        conversationId = idConversaReal
                                    )
                                    val res = RetrofitClient.getMessageService(context).enviarMensagem(req)
                                    if (res.isSuccessful) {
                                        textoMensagem = ""
                                        carregar()
                                    } else {
                                        Log.e("WTC_DEBUG", "Erro POST: ${res.code()}")
                                    }
                                } catch (e: Exception) {
                                    Log.e("WTC_DEBUG", "Falha no Envio: ${e.message}")
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Send, null, tint = Color(0xFF1976D2))
                    }
                }
            }
        }
    ) { p ->
        Box(modifier = Modifier.padding(p).fillMaxSize().background(Color(0xFFECE5DD))) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (listaMensagens.isEmpty()) {
                Text("Inicie a conversa!", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                    items(listaMensagens) { msg ->
                        val isOp = msg.senderRole?.equals("OPERADOR", ignoreCase = true) == true
                        BubbleChat(msg, isOp)
                    }
                }
            }
        }
    }
}

@Composable
fun BubbleChat(msg: MessageResponse, isOp: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = if (isOp) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = if (isOp) Color(0xFFDCF8C6) else Color.White),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = msg.content ?: "", fontSize = 15.sp, color = Color.Black)
                Text(
                    text = if (isOp) "WTC Operador" else "Cliente",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}