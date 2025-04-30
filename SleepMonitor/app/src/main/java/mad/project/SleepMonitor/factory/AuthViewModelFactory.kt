package mad.project.SleepMonitor.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mad.project.SleepMonitor.domain.repository.AuthRepository
import mad.project.SleepMonitor.viewmodels.AuthViewModel

// Фабрика, которая знает, как создать AnalyticsViewModel
class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}