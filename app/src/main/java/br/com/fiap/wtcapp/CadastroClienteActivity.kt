package br.com.fiap.wtcapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.model.CreateCustomerRequest
import br.com.fiap.wtcapp.network.RetrofitClient
import br.com.fiap.wtcapp.ui.theme.WTCTheme
import kotlinx.coroutines.launch

class CadastroClienteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CadastroClienteScreen { finish() } // finish() fecha a tela após salvar
                }
            }
        }
    }
}

@Composable
fun CadastroClienteScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var document by remember { mutableStateOf("") }
    var isVip by remember { mutableStateOf(false) }
    var isFidelidade by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Nova Empresa", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome da Empresa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = document,
            onValueChange = { document = it },
            label = { Text("CNPJ / Documento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Switches para VIP e Fidelidade
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Cliente VIP?")
            Switch(checked = isVip, onCheckedChange = { isVip = it })
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Programa de Fidelidade?")
            Switch(checked = isFidelidade, onCheckedChange = { isFidelidade = it })
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (name.isBlank() || document.isBlank()) {
                    Toast.makeText(context, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    scope.launch {
                        try {
                            val request = CreateCustomerRequest(
                                name = name,
                                document = document,
                                vip = isVip,
                                fidelidade = isFidelidade,
                                ativo = true
                            )
                            val service = RetrofitClient.getCustomerService(context)
                            val response = service.createCustomer(request)

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Empresa cadastrada!", Toast.LENGTH_SHORT).show()
                                onBack() // Volta para a lista
                            } else {
                                Toast.makeText(context, "Erro ao salvar: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Falha na rede", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White)
            else Text("Salvar Empresa")
        }
    }
}