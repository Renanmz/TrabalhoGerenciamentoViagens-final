package com.senac.gerenciamentoviagem.Bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.senac.gerenciamentoviagem.Model.Foto
import com.senac.gerenciamentoviagem.Model.User
import com.senac.gerenciamentoviagem.Model.Viagem

@Database(
    entities = [
        User::class,
        Viagem::class,
        Foto::class
    ],
    version = 3, // Incrementei a versão para incluir a tabela de fotos
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun viagemDao(): ViagemDao
    abstract fun fotoDao(): FotoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "viagem_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
