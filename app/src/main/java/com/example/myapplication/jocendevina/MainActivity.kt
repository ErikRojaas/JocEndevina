package com.example.myapplication.jocendevina


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Propiedades del juego
    private var numeroSecreto = 0
    private var contadorIntentos = 0
    private val records = mutableListOf<Pair<String, Int>>() // Lista para almacenar los récords

    // Variables de referencia de UI
    private lateinit var inputNumero: EditText
    private lateinit var historialTextView: TextView
    private lateinit var contadorTextView: TextView
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var listViewRecords: ListView
    private lateinit var btnBackToGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes de la interfaz
        viewFlipper = findViewById(R.id.view_flipper)
        inputNumero = findViewById(R.id.input_numero)
        historialTextView = findViewById(R.id.historial_intents)
        contadorTextView = findViewById(R.id.contador_intents)

        val btnComprovar: Button = findViewById(R.id.btn_comprovar)

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

        // Listener del botón "Ver récords"
        val btnHallOfFame: Button = findViewById(R.id.btn_hall_of_fame)
        btnHallOfFame.setOnClickListener {
            mostrarHallOfFame()
        }

        // Inicializar la lista de récords y el botón de volver al juego
        listViewRecords = findViewById(R.id.list_view_records)
        btnBackToGame = findViewById(R.id.btn_back_to_game)

        // Listener del botón "Tornar al joc"
        btnBackToGame.setOnClickListener {
            volverAlJuego()
        }
    }

    private fun validarIntento(numero: Int) {
        // Incrementar contador de intentos
        contadorIntentos++
        contadorTextView.text = "Intents: $contadorIntentos"

        when {
            numero > numeroSecreto -> {
                historialTextView.append("\n$numero és més baix")
            }
            numero < numeroSecreto -> {
                historialTextView.append("\n$numero és més alt")
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
        builder.setMessage("Has endevinat el número en $contadorIntentos intents. Vols guardar el teu récord?")
            .setPositiveButton("Sí") { dialog, _ ->
                // Llamamos a guardar el récord y luego reiniciamos el juego
                guardarRecord {
                    reiniciarJuego()  // Solo reinicia después de guardar el récord
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Si no quiere guardar el récord, reinicia directamente el juego
                reiniciarJuego()
                dialog.dismiss()
            }
        builder.show()
    }


    private fun guardarRecord(onRecordSaved: () -> Unit) {
        // Solicitar el nombre del jugador
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Guardar récord")
        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("Guardar") { dialog, _ ->
            val nombre = input.text.toString()
            records.add(Pair(nombre, contadorIntentos))
            // Aquí actualizas la lista en el ListView
            Toast.makeText(this, "Récord guardat!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()

            // Llamamos al callback para reiniciar el juego
            onRecordSaved()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
            // Si cancela, no hacemos nada, el juego solo se reinicia si lo pide explícitamente
        }
        builder.show()
    }



    private fun mostrarHallOfFame() {
        viewFlipper.displayedChild = 1 // Cambia a la segunda vista (Hall of Fame)
        val adapter = RecordAdapter(this, records.sortedBy { it.second })
        listViewRecords.adapter = adapter
    }

    private fun volverAlJuego() {
        viewFlipper.displayedChild = 0 // Regresar a la primera vista (Juego)
    }

    private fun reiniciarJuego() {
        numeroSecreto = Random.nextInt(1, 101)  // Generar nuevo número secreto
        contadorIntentos = 0  // Reiniciar contador de intentos
        contadorTextView.text = "Intents: 0"  // Actualizar UI
        historialTextView.text = "Historial d'intents"  // Limpiar historial
        Toast.makeText(this, "Nou número generat!", Toast.LENGTH_SHORT).show()
    }
}

