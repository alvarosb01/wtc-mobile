package br.com.fiap.wtcapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtcapp.model.RegisterRequest
import br.com.fiap.wtcapp.network.RetrofitClient
import br.com.fiap.wtcapp.ui.theme.WTCTheme
import kotlinx.coroutines.launch

class CadastroUsuarioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CadastroUsuarioScreen { finish() }
                }
            }
        }
    }
}

@Composable
fun CadastroUsuarioScreen(onSucesso: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var papelSelecionado by remember { mutableStateOf("CLIENTE") } // OPERADOR ou CLIENTE
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Criar Nova Conta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Seletor de Perfil (Role)
        Text("Selecione o tipo de acesso:", fontSize = 14.sp, color = Color.Gray)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = { papelSelecionado = "OPERADOR" }) {
                Text(
                    "Operador",
                    fontWeight = if (papelSelecionado == "OPERADOR") FontWeight.Bold else FontWeight.Normal,
                    color = if (papelSelecionado == "OPERADOR") Color(0xFF1976D2) else Color.Gray
                )
            }
            TextButton(onClick = { papelSelecionado = "CLIENTE" }) {
                Text(
                    "Cliente",
                    fontWeight = if (papelSelecionado == "CLIENTE") FontWeight.Bold else FontWeight.Normal,
                    color = if (papelSelecionado == "CLIENTE") Color(0xFF1976D2) else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail corporativo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha de acesso") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isBlank() || senha.isBlank()) {
                    Toast.makeText(context, "Campos obrigatórios vazios", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    scope.launch {
                        try {
                            val request = RegisterRequest(
                                email = email,
                                senha = senha,
                                role = papelSelecionado
                            )
                            // Chamada pública (authService)
                            val response = RetrofitClient.authService.registrarUsuario(request)

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Usuário cadastrado como $papelSelecionado!", Toast.LENGTH_LONG).show()
                                onSucesso()
                            } else {
                                Toast.makeText(context, "Erro: E-mail já cadastrado no WTC", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Falha na conexão com o servidor", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
            enabled = !isLoading
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Finalizar Cadastro", color = Color.White)
        }
    }
}