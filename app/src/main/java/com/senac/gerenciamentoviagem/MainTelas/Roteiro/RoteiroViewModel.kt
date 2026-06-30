package com.senac.gerenciamentoviagem.MainTelas.Roteiro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.Model.Viagem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.net.SocketTimeoutException
import java.io.InterruptedIOException

class RoteiroViewModel(
    private val repository: RoteiroRepository,
    private val viagemDao: ViagemDao
) : ViewModel() {
    var roteiroGerado by mutableStateOf("")
    var carregando by mutableStateOf(false)
    var viagemAtual by mutableStateOf<Viagem?>(null)
    var diasViagem by mutableStateOf(0)

    fun carregarViagem(viagemId: Int) {
        viewModelScope.launch {
            val viagem = viagemDao.getById(viagemId)
            viagemAtual = viagem
            viagem?.let {
                diasViagem = calcularDias(it.dataInicio, it.dataFinal)
            }
        }
    }

    private fun calcularDias(inicio: String, fim: String): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val dataIni = LocalDate.parse(inicio, formatter)
            val dataFim = LocalDate.parse(fim, formatter)
            ChronoUnit.DAYS.between(dataIni, dataFim).toInt() + 1
        } catch (e: Exception) {
            try {
                val dataIni = LocalDate.parse(inicio)
                val dataFim = LocalDate.parse(fim)
                ChronoUnit.DAYS.between(dataIni, dataFim).toInt() + 1
            } catch (e2: Exception) {
                5
            }
        }
    }

    fun gerarRoteiro(interesses: String) {
        val viagem = viagemAtual ?: return
        viewModelScope.launch {
            carregando = true
            try {
                roteiroGerado = repository.obterRoteiro(
                    cidade = viagem.destino,
                    dias = diasViagem,
                    interesses = interesses,
                    orcamento = viagem.orcamento,
                    tipoViagem = viagem.tipo.name
                )
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                roteiroGerado = "Erro HTTP ${e.code()}: ${errorBody ?: e.message()}"
            } catch (e: SocketTimeoutException) {
                roteiroGerado = "O servidor demorou muito para responder (Timeout). Tente novamente em instantes."
            } catch (e: InterruptedIOException) {
                roteiroGerado = "Conexão interrompida por demora na resposta. Tente novamente."
            } catch (e: Exception) {
                roteiroGerado = "Erro ao conectar com a IA: ${e.message}"
            } finally {
                carregando = false
            }
        }
    }
}
