package com.example.myapplication.jocendevina

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Propiedades del juego
    private var numeroSecreto = 0
    private var contadorIntentos = 0

    // Variables de referencia de UI
    private lateinit var inputNumero: EditText
    private lateinit var historialTextView: TextView
    private lateinit var contadorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes de la interfaz
        inputNumero = findViewById(R.id.input_numero)
        val btnComprovar: Button = findViewById(R.id.btn_comprovar)
        historialTextView = findViewById(R.id.historial_intents)
        contadorTextView = findViewById(R.id.contador_intents)

        // Generar el número aleatorio al iniciar la actividad
        reiniciarJuego()

        // Listener del botón "Comprovar"
        btnComprovar.setOnClickListener {
            val numeroIngresado = inputNumero.text.toString()

            // Comprobar si el campo está vacío
            if (numeroIngresado.isNotEmpty()) {
                val numero = numeroIngresado.toInt()
                validarIntento(numero)
            } else {
                Toast.makeText(this, "Per favor, escriu un número.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarIntento(numero: Int) {
        // Incrementar contador de intentos
        contadorIntentos++
        contadorTextView.text = "Intents: $contadorIntentos"

        when {
            numero > numeroSecreto -> {
                historialTextView.append("\n$numero és més baix")
                Log.i("INFO", "El número introduït és: $numero, però és més baix.")
            }
            numero < numeroSecreto -> {
                historialTextView.append("\n$numero és més alt")
                Log.i("INFO", "El número introduït és: $numero, però és més alt.")
            }
            else -> {
                historialTextView.append("\n$numero és correcte!")
                mostrarDialogFinal()
            }
        }

        // Limpiar el campo de entrada después del intento
        inputNumero.text.clear()
    }

    private fun mostrarDialogFinal() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Partida acabada!")
        builder.setMessage("Has endevinat el número en $contadorIntentos intents.")

        // Botón para reiniciar el juego
        builder.setPositiveButton("Tornar a jugar") { _, _ ->
            reiniciarJuego()
        }

        // Botón para finalizar sin reiniciar
        builder.setNegativeButton("Sortir") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun reiniciarJuego() {
        numeroSecreto = Random.nextInt(1, 101)  // Generar nuevo número secreto
        contadorIntentos = 0  // Reiniciar contador de intentos
        contadorTextView.text = "Intents: 0"  // Actualizar UI
        historialTextView.text = "Historial d'intents"  // Limpiar historial
        Toast.makeText(this, "Nou número generat!", Toast.LENGTH_SHORT).show()
    }
}
