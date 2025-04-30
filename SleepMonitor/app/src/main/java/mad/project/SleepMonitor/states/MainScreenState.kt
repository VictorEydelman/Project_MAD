package mad.project.SleepMonitor.states

import mad.project.SleepMonitor.domain.model.Profile
import mad.project.SleepMonitor.domain.model.TimePreference
import java.time.LocalTime

data class MainScreenState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val recommendedTimes: TimePreference? = null,
    val error: String? = null,
)
