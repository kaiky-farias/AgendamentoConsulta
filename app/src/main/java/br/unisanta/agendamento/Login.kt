package br.unisanta.agendamento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var btnSignUp: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var radioGroup: RadioGroup // Para escolher se é médico ou paciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        radioGroup = findViewById(R.id.radioGroup)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val userType = when (radioGroup.checkedRadioButtonId) {
                R.id.rbMedico -> "medico"
                else -> "paciente"
            }

            signUp(email, password, userType)
        }
    }

    private fun signUp(email: String, password: String, userType: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userData = hashMapOf(
                            "email" to email,
                            "userType" to userType // Médico ou Paciente
                        )

                        user?.uid?.let {
                            firestore.collection("users").document(it)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                    navigateToHome(userType)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao salvar dados no Firestore", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Erro ao criar usuário: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome(userType: String) {
        val intent = if (userType == "medico") {
            Intent(this, Medico::class.java)
        } else {
            Intent(this, Paciente::class.java)
        }
        startActivity(intent)
        finish()
    }
}