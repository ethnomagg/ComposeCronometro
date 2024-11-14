package com.example.composecronometro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// La clase principal de la aplicación
class MainActivity : ComponentActivity() {
    // Método de inicio de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el contenido de la pantalla
        setContent {
            Pantalla()
        }
    }
}

// Función composable que define la pantalla principal
@Composable
fun Pantalla() {
    // Estados del cronómetro
    var enMarcha by remember { mutableStateOf(false) }
    var tiempo by remember { mutableLongStateOf(0L) }
    var ultimoEstado by remember { mutableStateOf(EstadoBoton.START) }

    // Efecto de corrutina que actualiza el tiempo mientras el cronómetro está en marcha
    LaunchedEffect(enMarcha) {
        while (enMarcha) {
            delay(10) // Retardo de 10 milisegundos
            tiempo += 10 // Incremento del tiempo en 10 milisegundos
        }
    }

    // Función para formatear el tiempo en formato "mm:ss:cc"
    fun formatearTiempo(tiempoEnMs: Long): String {
        val minutos = (tiempoEnMs / 60000).toString().padStart(2, '0') // Obtiene los minutos
        val segundos = ((tiempoEnMs % 60000) / 1000).toString().padStart(2, '0') // Obtiene los segundos
        val centisegundos = ((tiempoEnMs % 1000) / 10).toString().padStart(2, '0') // Obtiene las centésimas de segundo
        return "$minutos:$segundos:$centisegundos" // Devuelve el tiempo formateado
    }

    // Obtiene el ancho de la pantalla
    val anchoPantalla = LocalConfiguration.current.screenWidthDp.dp

    // Estructura de la interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF434A45)), // Fondo de la pantalla
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.weight(1f)) // Espacio superior

        // Muestra el tiempo formateado
        Text(
            text = formatearTiempo(tiempo),
            color = Color.White,
            fontSize = 90.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f)) // Espacio intermedio

        // Fila con los botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón principal (Iniciar/Pausa/Continuar)
            Button(
                onClick = {
                    when (ultimoEstado) {
                        EstadoBoton.START -> {
                            enMarcha = true
                            ultimoEstado = EstadoBoton.PAUSE
                        }
                        EstadoBoton.PAUSE -> {
                            enMarcha = false
                            ultimoEstado = EstadoBoton.CONTINUE
                        }
                        EstadoBoton.CONTINUE -> {
                            enMarcha = true
                            ultimoEstado = EstadoBoton.PAUSE
                        }
                    }
                },
                modifier = Modifier.size(anchoPantalla * 0.25f),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (ultimoEstado == EstadoBoton.PAUSE) Color.Red else Color(0xFF10DE47)
                )
            ) {
                Text(
                    text = ultimoEstado.text,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    textAlign = TextAlign.Center
                )
            }

            // Botón de Reiniciar
            TextButton(
                onClick = {
                    enMarcha = false
                    tiempo = 0
                    ultimoEstado = EstadoBoton.START
                },
                modifier = Modifier.width(anchoPantalla * 0.25f)
            ) {
                Text(
                    text = "Reiniciar",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Enum que define los estados del botón principal
enum class EstadoBoton(val text: String) {
    START("Inicio"), PAUSE("Pausa"), CONTINUE("Continuar")
}