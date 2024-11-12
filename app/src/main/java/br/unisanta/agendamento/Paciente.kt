package br.unisanta.agendamento

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Paciente : AppCompatActivity() {

        private lateinit var firestore: FirebaseFirestore
        private lateinit var btnMarcarConsulta: Button
        private lateinit var calendarView: CalendarView
        private lateinit var timePicker: TimePicker
        private var selectedDate: String = ""
        private var selectedTime: String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_paciente)

            firestore = FirebaseFirestore.getInstance()
            btnMarcarConsulta = findViewById(R.id.btnMarcarConsulta)
            calendarView = findViewById(R.id.calendarView)
            timePicker = findViewById(R.id.timePicker)


            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormat.format(calendar.time)
            }


            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            }

            btnMarcarConsulta.setOnClickListener {
                if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                    val consulta = hashMapOf(
                        "data" to selectedDate,
                        "hora" to selectedTime,
                        "status" to "pendente"
                    )

                    firestore.collection("consultas").add(consulta)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Consulta marcada com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao marcar consulta. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }