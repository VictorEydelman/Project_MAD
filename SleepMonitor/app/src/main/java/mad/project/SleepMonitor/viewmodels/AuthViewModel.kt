package mad.project.SleepMonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mad.project.SleepMonitor.data.network.dto.AuthRequest
import mad.project.SleepMonitor.domain.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val authRequest = AuthRequest(username, password)
            val result = authRepository.login(authRequest)
            if (result.isSuccess) {
                onResult(true, "Login successful: ${result.getOrNull()?.username}")
            } else {
                onResult(false, "Login failed: ${result.exceptionOrNull()?.message ?: "Unknown error"}")
            }
        }
    }

    fun register(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val authRequest = AuthRequest(username, password)
            val result = authRepository.register(authRequest)
            if (result.isSuccess) {
                onResult(true, "Registration successful: ${result.getOrNull()?.username}")
            } else {
                onResult(false, "Registration failed: ${result.exceptionOrNull()?.message ?: "Unknown error"}")
            }
        }
    }
}