package no.uio.ifi.in2000.mariish.oblig2.ui.party

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.mariish.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.mariish.oblig2.model.alpacas.PartyInfo

class PartyViewModel(
    private val partyId:String,
    private val repository:AlpacaPartiesRepository
) : ViewModel()
{

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val party:PartyInfo ) : UiState()
        data class Error(val errorMessage: String) : UiState()
        data object Empty : UiState()
    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    init{
        getAlpacaInfo()
    }

    private fun getAlpacaInfo(){
        viewModelScope.launch {
            try {
                val party= repository.getID(partyId)
                _uiState.value = if (party==null) {
                    UiState.Empty
                } else {
                    UiState.Success(party)
                }
            }catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ukjent feil")
            }
        }

    }


    companion object{
        fun provideFactory(partyId: String, repository: AlpacaPartiesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PartyViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PartyViewModel(partyId, repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }


}