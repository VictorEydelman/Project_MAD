package mad.project.SleepMonitor.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlarmViewModel : ViewModel() {
    private val _isAlarmEnabled = MutableStateFlow(false)
    val isAlarmEnabled: StateFlow<Boolean> = _isAlarmEnabled

    private val _isRepeatEnabled = MutableStateFlow(false)
    val isRepeatEnabled: StateFlow<Boolean> = _isRepeatEnabled

    fun setAlarmEnabled(enabled: Boolean) {
        _isAlarmEnabled.value = enabled
    }

    fun setRepeatEnabled(enabled: Boolean) {
        _isRepeatEnabled.value = enabled
    }
}
