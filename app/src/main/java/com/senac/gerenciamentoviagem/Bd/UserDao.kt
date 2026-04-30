package com.senac.gerenciamentoviagem.Bd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.senac.gerenciamentoviagem.Model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE TRIM(email) = :email AND TRIM(senha) = :senha")
    suspend fun login(email: String, senha: String): User?

    @Query("SELECT * FROM users")
    suspend fun findAll(): List<User>
}