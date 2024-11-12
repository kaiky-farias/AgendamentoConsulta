package br.unisanta.agendamento

data class Consulta(
    val data: String = "",
    val hora: String = "",
    val status: String = "pendente"
)