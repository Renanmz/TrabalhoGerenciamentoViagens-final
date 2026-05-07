package com.senac.gerenciamentoviagem.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Entity(tableName = "viagem")
@Serializable
data class Viagem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val destino: String,
    val tipo: Tipo,
    val dataInicio : String,
    val dataFinal: String,
    val orcamento: Float,
    val userId: Int
)
@Serializable
enum class Tipo(){
    Lazer,
    Negocio
}