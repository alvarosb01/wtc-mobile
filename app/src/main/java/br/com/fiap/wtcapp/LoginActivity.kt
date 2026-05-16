package br.com.fiap.wtcapp

import android.app.Activity
import android.content.Intent
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
import br.com.fiap.wtcapp.ui.theme.WTCTheme
import br.com.fiap.wtcapp.model.LoginRequest
import br.com.fiap.wtcapp.network.RetrofitClient
import br.com.fiap.wtcapp.network.TokenManager
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTCTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Variável para controle de UI
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WTC São Paulo",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Faça login para continuar",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isBlank() || senha.isBlank()) {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    scope.launch {
                        try {
                            // No login enviamos apenas email e senha.
                            // A Role quem nos diz é o servidor na resposta.
                            val request = LoginRequest(email = email, senha = senha)
                            val response = RetrofitClient.authService.login(request)

                            if (response.isSuccessful) {
                                val loginResponse = response.body()

                                if (loginResponse != null) {
                                    val token = loginResponse.token
                                    val role = loginResponse.role
                                    val idDoBanco = loginResponse.userId // <--- PEGAMOS O ID QUE VEIO DO JAVA

                                    val tokenManager = TokenManager(context)

                                    // 2. SALVAMOS O ID REAL QUE VEIO DO SERVIDOR (Não use String()!)
                                    tokenManager.saveSession(token, role, idDoBanco)

                                    println("LOGIN SUCESSO: Role $role e ID $idDoBanco salvos")

                                    Toast.makeText(context, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                                    context.startActivity(Intent(context, HomeActivity::class.java))
                                    (context as? Activity)?.finish()
                                }
                            } else {
                                Toast.makeText(context, "Credenciais inválidas", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Entrar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LINK PARA CADASTRO DE NOVO USUÁRIO
        TextButton(
            onClick = {
                context.startActivity(Intent(context, CadastroUsuarioActivity::class.java))
            },
            enabled = !isLoading
        ) {
            Text(
                text = "Não tem uma conta? Cadastre-se aqui",
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}