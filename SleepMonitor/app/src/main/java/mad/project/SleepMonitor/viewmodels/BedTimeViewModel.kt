package mad.project.SleepMonitor.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BedTimeViewModel : ViewModel() {
    private val _isSleepReminderEnabled = MutableStateFlow(false)
    val isSleepReminderEnabled: StateFlow<Boolean> get() = _isSleepReminderEnabled

    private val _isBedReminderEnabled = MutableStateFlow(false)
    val isBedReminderEnabled: StateFlow<Boolean> get() = _isBedReminderEnabled

    private val _isRepeatEnabled = MutableStateFlow(false)
    val isRepeatEnabled: StateFlow<Boolean> get() = _isRepeatEnabled

    fun setSleepReminderEnabled(enabled: Boolean) {
        _isSleepReminderEnabled.value = enabled
    }

    fun setBedReminderEnabled(enabled: Boolean) {
        _isBedReminderEnabled.value = enabled
    }

    fun setRepeatEnabled(enabled: Boolean) {
        _isRepeatEnabled.value = enabled
    }

}
