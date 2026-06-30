package com.senac.gerenciamentoviagem.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "fotos",
    foreignKeys = [
        ForeignKey(
            entity = Viagem::class,
            parentColumns = ["id"],
            childColumns = ["viagemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Foto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val viagemId: Int,
    val uri: String // Armazenamos a URI como String
)
