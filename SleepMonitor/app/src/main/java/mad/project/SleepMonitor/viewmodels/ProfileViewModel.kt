package mad.project.SleepMonitor.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mad.project.SleepMonitor.data.network.dto.ProfileData
import mad.project.SleepMonitor.data.network.dto.UpdateProfileRequest
import mad.project.SleepMonitor.data.repository.ProfileRepository
import mad.project.SleepMonitor.domain.model.Profile
import mad.project.SleepMonitor.screens.Gender
import mad.project.SleepMonitor.screens.Preference
import mad.project.SleepMonitor.states.ProfileScreenState

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    init {
        fetchProfile()
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun fetchProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result: Profile? = repository.getProfile()

            if (result != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        profile = result,
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

    fun saveProfile(
        name: String,
        surname: String,
        birthday: String,
        gender: Gender?,
        physicalCondition: Preference?,
        caffeineUsage: Preference?,
        alcoholUsage: Preference?
    ) {
        viewModelScope.launch {
            val currentProfile = state.value.profile

            if (currentProfile != null) {
                val request = UpdateProfileRequest(
                    name = name,
                    surname = surname,
                    birthday = birthday,
                    gender = gender?.displayName,
                    physicalCondition = physicalCondition?.name,
                    caffeineUsage = caffeineUsage?.name,
                    alcoholUsage = alcoholUsage?.name,
                    alarmRecurring = currentProfile.alarmRecurring,
                    alarmTemporary = currentProfile.alarmTemporary,
                    bedTimeRecurring = currentProfile.bedTimeRecurring,
                    bedTimeTemporary = currentProfile.bedTimeTemporary
                )
                Log.d("ViewModel", "Sending JSON: ${Gson().toJson(request)}")
                repository.updateProfile(request)
            } else {
                Log.e("ProfileViewModel", "Attempted to saveProfile with null profile state")
            }
        }
    }

}