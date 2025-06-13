package com.amipet.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.User
import com.amipet.app.model.UserType
import com.amipet.app.repository.AuthRepository
import com.amipet.app.repository.UserRepository
import com.amipet.app.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.navigation.NavController

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: StateFlow<ForgotPasswordState> = _forgotPasswordState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoggedInState = MutableStateFlow(false)
    val isLoggedInState: StateFlow<Boolean> = _isLoggedInState

    private var navController: NavController? = null

    init {
        _isLoggedInState.value = authRepository.isLoggedIn()
        if (isLoggedIn()) {
            loadCurrentUser()
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                if (response.success) {
                    loadCurrentUser()
                    _loginState.value = LoginState.Success
                    _isLoggedInState.value = true
                    navController?.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    _loginState.value = LoginState.Error(response.error ?: "Falha ao fazer login")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun register(name: String, email: String, password: String, phone: String? = null, address: String? = null) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.register(name, email, password, phone, address)
                if (response.success) {
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(response.error ?: "Falha ao registrar")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun forgotPassword(email: String) {
        _forgotPasswordState.value = ForgotPasswordState.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.forgotPassword(email)
                if (response.success) {
                    _forgotPasswordState.value = ForgotPasswordState.Success
                } else {
                    _forgotPasswordState.value = ForgotPasswordState.Error(response.error ?: "Falha ao enviar e-mail de recuperação")
                }
            } catch (e: Exception) {
                _forgotPasswordState.value = ForgotPasswordState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _currentUser.value = null
            _isLoggedInState.value = false
            navController?.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val response = userRepository.getCurrentUser()
                if (response.success && response.data != null) {
                    _currentUser.value = response.data
                } else if (authRepository.isLoggedIn() && (sessionManager.getUserId() == "admin_user_id")) {
                    _currentUser.value = User(id = "admin_user_id", name = "Admin", email = "admin", type = UserType.ADMIN, phone = null, address = null, profileImageUrl = null)
                }
            } catch (e: Exception) {
                // Falha ao carregar usuário
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    fun isLoggedIn(): Boolean {
        return _isLoggedInState.value
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }

    sealed class ForgotPasswordState {
        object Idle : ForgotPasswordState()
        object Loading : ForgotPasswordState()
        object Success : ForgotPasswordState()
        data class Error(val message: String) : ForgotPasswordState()
    }
}