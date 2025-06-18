package no.uio.ifi.in2000.mariish.oblig2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.mariish.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.mariish.oblig2.model.alpacas.PartyInfo


class HomeScreenViewModel:ViewModel(){

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val parties: List<PartyInfo>) : UiState()
        data class Error(val errorMessage: String) : UiState()
        data object Empty : UiState()
    }


    private val repository=AlpacaPartiesRepository()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init{
        getAlpacaParties()
    }

    private fun getAlpacaParties(){
        viewModelScope.launch {
            try {
                val parties = repository.getParties()
                _uiState.value = if (parties.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(parties)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }

    }




}