package com.senac.gerenciamentoviagem.Bd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.senac.gerenciamentoviagem.Model.Foto
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoDao {
    @Query("SELECT * FROM fotos WHERE viagemId = :viagemId")
    fun getFotosPorViagem(viagemId: Int): Flow<List<Foto>>

    @Insert
    suspend fun inserir(foto: Foto)

    @Delete
    suspend fun deletar(foto: Foto)
}
