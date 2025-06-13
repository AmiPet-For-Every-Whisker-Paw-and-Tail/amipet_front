package com.amipet.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amipet.app.model.Animal
import com.amipet.app.model.Match
import com.amipet.app.model.MatchStatus
import com.amipet.app.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MatchViewModel(private val petRepository: PetRepository) : ViewModel() {
    private val _matchesState = MutableStateFlow<MatchesState>(MatchesState.Loading)
    val matchesState: StateFlow<MatchesState> = _matchesState

    fun loadMatches() {
        _matchesState.value = MatchesState.Loading
        viewModelScope.launch {
            try {
                val response = petRepository.getAvailablePets()
                if (response.success && response.data != null) {
                    val matches = response.data.map { animal ->
                        Match(
                            id = "${animal.id}_${Date().time}", // Unique ID combining animalId and timestamp
                            userId = "currentUserId", // Replace with actual user ID (e.g., from SessionManager)
                            animalId = animal.id,
                            timestamp = Date().time,
                            status = MatchStatus.PENDING
                        )
                    }
                    _matchesState.value = if (matches.isEmpty()) MatchesState.Empty else MatchesState.Success(matches)
                } else {
                    _matchesState.value = MatchesState.Error("Erro ao carregar matches: ${response.message}")
                }
            } catch (e: Exception) {
                _matchesState.value = MatchesState.Error("Erro ao carregar matches: ${e.message}")
            }
        }
    }

    sealed class MatchesState {
        object Loading : MatchesState()
        object Empty : MatchesState()
        data class Success(val matches: List<Match>) : MatchesState()
        data class Error(val message: String) : MatchesState()
    }
}

class MatchViewModelFactory(private val petRepository: PetRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchViewModel::class.java)) {
            return MatchViewModel(petRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}