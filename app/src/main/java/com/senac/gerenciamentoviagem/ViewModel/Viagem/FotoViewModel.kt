package com.senac.gerenciamentoviagem.ViewModel.Viagem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Bd.FotoDao
import com.senac.gerenciamentoviagem.Model.Foto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FotoViewModel(private val fotoDao: FotoDao) : ViewModel() {

    private val _fotos = MutableStateFlow<List<Foto>>(emptyList())
    val fotos: StateFlow<List<Foto>> = _fotos

    fun carregarFotos(viagemId: Int) {
        viewModelScope.launch {
            fotoDao.getFotosPorViagem(viagemId).collect {
                _fotos.value = it
            }
        }
    }

    fun adicionarFoto(viagemId: Int, uri: String) {
        viewModelScope.launch {
            val novaFoto = Foto(viagemId = viagemId, uri = uri)
            fotoDao.inserir(novaFoto)
        }
    }

    fun deletarFoto(foto: Foto) {
        viewModelScope.launch {
            fotoDao.deletar(foto)
        }
    }
}
