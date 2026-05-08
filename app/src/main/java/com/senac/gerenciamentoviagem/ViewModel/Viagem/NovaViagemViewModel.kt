package com.senac.gerenciamentoviagem.ViewModel.Viagem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.Model.Tipo
import com.senac.gerenciamentoviagem.Model.Viagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NovaViagemViewModel(
    private val viagemDao: ViagemDao
) : ViewModel() {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val datatime = LocalDateTime.now().format(formatter)
    val data = datatime.toString()

    private val _destino = MutableStateFlow("")
    val destino = _destino.asStateFlow()

    private val _tipo = MutableStateFlow(Tipo.Lazer)
    val tipo = _tipo.asStateFlow()

    private val _dataInicio = MutableStateFlow(data)
    val dataInicio = _dataInicio.asStateFlow()

    private val _dataFinal = MutableStateFlow(data)
    val dataFinal = _dataFinal.asStateFlow()

    private val _orcamento = MutableStateFlow("")
    val orcamento = _orcamento.asStateFlow()

    fun onDestinoChange(v: String) { _destino.value = v }
    fun onTipoChange(v: Tipo) { _tipo.value = v }
    fun onDataInicioChange(v: String) { _dataInicio.value = v }
    fun onDataFinalChange(v: String) { _dataFinal.value = v }
    fun onOrcamentoChange(v: String) { _orcamento.value = v }

    fun salvar(userId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            viagemDao.insert(
                Viagem(
                    destino = _destino.value,
                    tipo = _tipo.value,
                    dataInicio = _dataInicio.value,
                    dataFinal = _dataFinal.value,
                    orcamento = _orcamento.value.toFloatOrNull() ?: 0f,
                    userId = userId
                )
            )
            onSuccess()
        }
    }
    fun atualizar(viagem: Viagem, onSuccess: () -> Unit) {
        viewModelScope.launch {
            viagemDao.update(viagem)
            onSuccess()
        }
    }
    fun limparCampos() {
        _destino.value = ""
        _tipo.value = Tipo.Lazer
        _dataInicio.value = data
        _dataFinal.value = data
        _orcamento.value = ""
    }
}