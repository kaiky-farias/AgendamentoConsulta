package br.unisanta.agendamento

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Medico : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConsultaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ConsultaAdapter()
        recyclerView.adapter = adapter

        loadConsultas()
    }

    private fun loadConsultas() {
        firestore.collection("consultas").get()
            .addOnSuccessListener { result ->
                val consultas = result.map { it.toObject(Consulta::class.java) }
                adapter.submitList(consultas)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar consultas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}