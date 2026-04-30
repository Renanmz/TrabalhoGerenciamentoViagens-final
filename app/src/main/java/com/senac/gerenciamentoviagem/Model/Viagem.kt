package com.senac.gerenciamentoviagem.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "viagem")
data class Viagem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val Destino: String,
    val Tipo: Tipo,
    val DataInicio : LocalDateTime = LocalDateTime.now(),
    val DataFinal: LocalDateTime = LocalDateTime.now(),
    val Orcamento: Float,
    val UserId: Int
)
enum class Tipo(){
    Lazer,
    Negocio
}