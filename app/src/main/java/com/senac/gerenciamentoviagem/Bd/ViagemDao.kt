package com.senac.gerenciamentoviagem.Bd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.senac.gerenciamentoviagem.Model.Viagem

@Dao
interface ViagemDao {

    @Insert
    suspend fun insert(viagem: Viagem)

    @Update
    suspend fun update(viagem: Viagem)

    @Delete
    suspend fun delete(viagem: Viagem)

    @Query("SELECT * FROM viagem WHERE id = :id")
    suspend fun getById(id: Int): Viagem?

    @Query("SELECT * FROM viagem WHERE userId = :userId")
    suspend fun findByUser(userId: Int): List<Viagem>

    @Query("""
SELECT * FROM viagem
    WHERE LOWER(destino) = LOWER(:cidade)
    AND date(:dataAtual) BETWEEN date(dataInicio) AND date(dataFinal)
    LIMIT 1
""")
    suspend fun buscarViagemAtual(
        cidade: String,
        dataAtual: String
    ): Viagem?
}
