package com.senac.gerenciamentoviagem.Bd

import androidx.room.TypeConverter
import com.senac.gerenciamentoviagem.Model.Tipo
import java.time.LocalDateTime

class DatabaseConverters {

    @TypeConverter
    fun fromTipo(tipo: Tipo): String {
        return tipo.name
    }

    @TypeConverter
    fun toTipo(value: String): Tipo {
        return Tipo.valueOf(value)
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}