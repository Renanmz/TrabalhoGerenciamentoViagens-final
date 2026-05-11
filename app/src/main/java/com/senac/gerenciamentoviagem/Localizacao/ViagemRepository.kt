package com.senac.gerenciamentoviagem.Localizacao

import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.Model.Viagem

class ViagemRepository(
    private val dao: ViagemDao
) {

    suspend fun buscarViagemAtual(
        cidade: String,
        dataAtual: String
    ): Viagem? {

        return dao.buscarViagemAtual(cidade, dataAtual)
    }
}