package com.senac.gerenciamentoviagem.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "viagem")
data class Viagem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val destino: String,
    val tipo: Tipo,
    val dataInicio : LocalDateTime = LocalDateTime.now(),
    val dataFinal: LocalDateTime = LocalDateTime.now(),
    val orcamento: Float,
    val userId: Int
)
enum class Tipo(){
    Lazer,
    Negocio
}