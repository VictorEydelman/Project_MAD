package mad.project.SleepMonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.domain.repository.AnalyticsRepository
import mad.project.SleepMonitor.ui.abilities.TimeRange
import mad.project.SleepMonitor.util.Resource
import mad.project.SleepMonitor.states.AnalyticsScreenState


class AnalyticsViewModel(
    private val repository: AnalyticsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsScreenState())
    val state: StateFlow<AnalyticsScreenState> = _state.asStateFlow()

    init {
        fetchReportData(TimeRange.ALL)
    }

    fun onTimeRangeSelected(newTimeRange: TimeRange) {
        if (newTimeRange != _state.value.selectedTimeRange || _state.value.report == null) {
            _state.update { it.copy(selectedTimeRange = newTimeRange) }
            fetchReportData(newTimeRange)
        }
    }

    private fun fetchReportData(timeRange: TimeRange) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }


            val result: Resource<Report> = when (timeRange) {
                TimeRange.DAY -> repository.getDailyReport()
                TimeRange.WEEK -> repository.getWeeklyReport()
                TimeRange.ALL -> repository.getAllTimeReport()
            }

            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            report = result.data,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }
}