package mad.project.SleepMonitor.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mad.project.SleepMonitor.data.repository.ProfileRepository
import mad.project.SleepMonitor.domain.model.Profile
import mad.project.SleepMonitor.domain.model.TimePreference
import mad.project.SleepMonitor.domain.repository.AnalyticsRepository
import mad.project.SleepMonitor.states.MainScreenState
import mad.project.SleepMonitor.util.Resource

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
class MainViewModel(
    private val analyticsRepository: AnalyticsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result: Profile? = profileRepository.getProfile()
            val times = analyticsRepository.getRecommendedTimes()

            if (result != null && times is Resource.Success) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        profile = result,
                        recommendedTimes = times.data,
                        error = null
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load profile data"
                    )
                }
            }
        }
    }

}