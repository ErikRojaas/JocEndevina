package com.example.myapplication.jocendevina;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Propiedades del juego
    private int numeroSecreto = 0;
    private int contadorIntentos = 0;
    private final List<Pair<String, Integer>> records = new ArrayList<>(); // Lista para almacenar los récords

    // Variables de referencia de UI
    private EditText inputNumero;
    private TextView historialTextView;
    private TextView contadorTextView;
    private ViewFlipper viewFlipper;
    private ListView listViewRecords;
    private Button btnBackToGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes de la interfaz
        viewFlipper = findViewById(R.id.view_flipper);
        inputNumero = findViewById(R.id.input_numero);
        historialTextView = findViewById(R.id.historial_intents);
        contadorTextView = findViewById(R.id.contador_intents);

        Button btnComprovar = findViewById(R.id.btn_comprovar);

        // Generar el número aleatorio al iniciar la actividad
        reiniciarJuego();

        // Listener del botón "Comprovar"
        btnComprovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeroIngresado = inputNumero.getText().toString();

                // Comprobar si el campo está vacío
                if (!numeroIngresado.isEmpty()) {
                    int numero = Integer.parseInt(numeroIngresado);
                    validarIntento(numero);
                } else {
                    Toast.makeText(MainActivity.this, "Per favor, escriu un número.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener del botón "Ver récords"
        Button btnHallOfFame = findViewById(R.id.btn_hall_of_fame);
        btnHallOfFame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarHallOfFame();
            }
        });

        // Inicializar la lista de récords y el botón de volver al juego
        listViewRecords = findViewById(R.id.list_view_records);
        btnBackToGame = findViewById(R.id.btn_back_to_game);

        // Listener del botón "Tornar al joc"
        btnBackToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAlJuego();
            }
        });
    }

    private void validarIntento(int numero) {
        // Incrementar contador de intentos
        contadorIntentos++;
        contadorTextView.setText("Intents: " + contadorIntentos);

        if (numero > numeroSecreto) {
            historialTextView.append("\n" + numero + " és més baix");
        } else if (numero < numeroSecreto) {
            historialTextView.append("\n" + numero + " és més alt");
        } else {
            historialTextView.append("\n" + numero + " és correcte!");
            mostrarDialogFinal();
        }

        // Limpiar el campo de entrada después del intento
        inputNumero.setText("");
    }

    private void mostrarDialogFinal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Partida acabada!")
                .setMessage("Has endevinat el número en " + contadorIntentos + " intents. Vols guardar el teu récord?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Llamamos a guardar el récord y luego reiniciamos el juego
                    guardarRecord(() -> reiniciarJuego());  // Solo reinicia después de guardar el récord
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Si no quiere guardar el récord, reinicia directamente el juego
                    reiniciarJuego();
                    dialog.dismiss();
                })
                .show();
    }

    private void guardarRecord(Runnable onRecordSaved) {
        // Solicitar el nombre del jugador
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Guardar récord");
        EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = input.getText().toString();
                    records.add(new Pair<>(nombre, contadorIntentos));
                    // Aquí actualizas la lista en el ListView
                    Toast.makeText(MainActivity.this, "Récord guardat!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    // Llamamos al callback para reiniciar el juego
                    onRecordSaved.run();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                    // Si cancela, no hacemos nada, el juego solo se reinicia si lo pide explícitamente
                })
                .show();
    }

    private void mostrarHallOfFame() {
        viewFlipper.setDisplayedChild(1); // Cambia a la segunda vista (Hall of Fame)
        RecordAdapter adapter = new RecordAdapter(this, records);
        listViewRecords.setAdapter(adapter);
    }

    private void volverAlJuego() {
        viewFlipper.setDisplayedChild(0); // Regresar a la primera vista (Juego)
    }

    private void reiniciarJuego() {
        numeroSecreto = new Random().nextInt(100) + 1;  // Generar nuevo número secreto (1 a 100)
        contadorIntentos = 0;  // Reiniciar contador de intentos
        contadorTextView.setText("Intents: 0");  // Actualizar UI
        historialTextView.setText("Historial d'intents");  // Limpiar historial
        Toast.makeText(this, "Nou número generat!", Toast.LENGTH_SHORT).show();
    }

    // Clase para representar un par (nombre, intentos)
    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }

    // Adaptador de récords
    public static class RecordAdapter extends ArrayAdapter<Pair<String, Integer>> {
        public RecordAdapter(Context context, List<Pair<String, Integer>> records) {
            super(context, 0, records);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Obtén el dato para la posición actual
            Pair<String, Integer> record = getItem(position);

            // Usa un layout para cada elemento de la lista
            View view = (convertView != null) ? convertView : LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);

            // Encuentra las vistas
            TextView text1 = view.findViewById(android.R.id.text1);
            TextView text2 = view.findViewById(android.R.id.text2);

            // Configura el texto de las vistas
            if (record != null) {
                text1.setText(record.first);  // Nombre del jugador
                text2.setText("Intentos: " + record.second);  // Número de intentos
            }

            return view;
        }
    }
}
