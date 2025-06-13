package com.amipet.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amipet.app.model.Animal
import com.amipet.app.model.Match
import com.amipet.app.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetViewModel(private val petRepository: PetRepository) : ViewModel() {
    
    private val _petsState = MutableStateFlow<PetsState>(PetsState.Loading)
    val petsState: StateFlow<PetsState> = _petsState
    
    private val _petDetailState = MutableStateFlow<PetDetailState>(PetDetailState.Idle)
    val petDetailState: StateFlow<PetDetailState> = _petDetailState
    
    private val _swipeActionState = MutableStateFlow<SwipeActionState>(SwipeActionState.Idle)
    val swipeActionState: StateFlow<SwipeActionState> = _swipeActionState
    
    init {
        loadPets()
    }
    
    fun loadPets() {
        _petsState.value = PetsState.Loading
        
        viewModelScope.launch {
            try {
                val response = petRepository.getAvailablePets()
                if (response.success && response.data != null) {
                    if (response.data.isEmpty()) {
                        _petsState.value = PetsState.Empty
                    } else {
                        _petsState.value = PetsState.Success(response.data)
                    }
                } else {
                    _petsState.value = PetsState.Error(response.error ?: "Falha ao carregar pets")
                }
            } catch (e: Exception) {
                _petsState.value = PetsState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun getPetById(id: String) {
        _petDetailState.value = PetDetailState.Loading
        
        viewModelScope.launch {
            try {
                val response = petRepository.getPetById(id)
                if (response.success && response.data != null) {
                    _petDetailState.value = PetDetailState.Success(response.data)
                } else {
                    _petDetailState.value = PetDetailState.Error(response.error ?: "Falha ao carregar detalhes do pet")
                }
            } catch (e: Exception) {
                _petDetailState.value = PetDetailState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun likePet(id: String) {
        _swipeActionState.value = SwipeActionState.Loading
        
        viewModelScope.launch {
            try {
                val response = petRepository.likePet(id)
                if (response.success) {
                    _swipeActionState.value = SwipeActionState.Success(SwipeAction.LIKE, response.data)
                } else {
                    _swipeActionState.value = SwipeActionState.Error(response.error ?: "Falha ao curtir pet")
                }
            } catch (e: Exception) {
                _swipeActionState.value = SwipeActionState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun dislikePet(id: String) {
        _swipeActionState.value = SwipeActionState.Loading
        
        viewModelScope.launch {
            try {
                val response = petRepository.dislikePet(id)
                if (response.success) {
                    _swipeActionState.value = SwipeActionState.Success(SwipeAction.DISLIKE, null)
                } else {
                    _swipeActionState.value = SwipeActionState.Error(response.error ?: "Falha ao descartar pet")
                }
            } catch (e: Exception) {
                _swipeActionState.value = SwipeActionState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun searchPets(species: String? = null, breed: String? = null, age: String? = null, size: String? = null) {
        _petsState.value = PetsState.Loading
        
        viewModelScope.launch {
            try {
                val response = petRepository.searchPets(species, breed, age, size)
                if (response.success && response.data != null) {
                    if (response.data.isEmpty()) {
                        _petsState.value = PetsState.Empty
                    } else {
                        _petsState.value = PetsState.Success(response.data)
                    }
                } else {
                    _petsState.value = PetsState.Error(response.error ?: "Falha ao buscar pets")
                }
            } catch (e: Exception) {
                _petsState.value = PetsState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun resetSwipeActionState() {
        _swipeActionState.value = SwipeActionState.Idle
    }
    
    sealed class PetsState {
        object Loading : PetsState()
        object Empty : PetsState()
        data class Success(val pets: List<Animal>) : PetsState()
        data class Error(val message: String) : PetsState()
    }
    
    sealed class PetDetailState {
        object Idle : PetDetailState()
        object Loading : PetDetailState()
        data class Success(val pet: Animal) : PetDetailState()
        data class Error(val message: String) : PetDetailState()
    }
    
    sealed class SwipeActionState {
        object Idle : SwipeActionState()
        object Loading : SwipeActionState()
        data class Success(val action: SwipeAction, val match: Match?) : SwipeActionState()
        data class Error(val message: String) : SwipeActionState()
    }
    
    enum class SwipeAction {
        LIKE,
        DISLIKE
    }
}
