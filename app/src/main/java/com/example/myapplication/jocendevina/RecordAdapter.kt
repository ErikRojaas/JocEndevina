package com.example.myapplication.jocendevina

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class RecordAdapter(context: Context, records: List<Pair<String, Int>>) :
    ArrayAdapter<Pair<String, Int>>(context, 0, records) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Obtén el dato para la posición actual
        val record = getItem(position)!!

        // Usa un layout para cada elemento de la lista
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        // Encuentra las vistas
        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)

        // Configura el texto de las vistas
        text1.text = record.first  // Nombre del jugador
        text2.text = "Intentos: ${record.second}"  // Número de intentos

        return view
    }
}
