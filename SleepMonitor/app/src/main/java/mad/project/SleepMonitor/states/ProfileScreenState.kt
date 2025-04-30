package mad.project.SleepMonitor.states

import mad.project.SleepMonitor.data.network.dto.ProfileData
import mad.project.SleepMonitor.domain.model.Profile

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null,
)
