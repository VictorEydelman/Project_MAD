package mad.project.SleepMonitor.states

import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.ui.abilities.TimeRange

data class AnalyticsScreenState(
    val isLoading: Boolean = false,
    val report: Report? = null,
    val error: String? = null,
    val selectedTimeRange: TimeRange = TimeRange.ALL
)