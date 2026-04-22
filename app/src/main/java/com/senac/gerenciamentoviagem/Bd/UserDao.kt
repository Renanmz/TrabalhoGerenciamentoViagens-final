package com.senac.gerenciamentoviagem.Bd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND senha = :senha")
    suspend fun login(email: String, senha: String): User?

}