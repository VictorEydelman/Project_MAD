package mad.project.SleepMonitor.factory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mad.project.SleepMonitor.data.repository.ProfileRepository
import mad.project.SleepMonitor.domain.repository.AnalyticsRepository
import mad.project.SleepMonitor.viewmodels.MainViewModel

class MainViewModelFactory(
    private val analyticsRepository: AnalyticsRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(analyticsRepository, profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
