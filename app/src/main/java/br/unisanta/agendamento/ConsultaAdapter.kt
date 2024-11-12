package br.unisanta.agendamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class ConsultaAdapter : RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder>() {

    private var consultas: List<Consulta> = listOf()


    fun submitList(consultas: List<Consulta>) {
        this.consultas = consultas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultaViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consulta, parent, false)
        return ConsultaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultaViewHolder, position: Int) {
        val consulta = consultas[position]
        holder.bind(consulta)
    }

    override fun getItemCount(): Int = consultas.size

    inner class ConsultaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDataHora: TextView = itemView.findViewById(R.id.tvDataHora)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(consulta: Consulta) {
            tvDataHora.text = "${consulta.data} - ${consulta.hora}"
            tvStatus.text = consulta.status
        }
    }
}
