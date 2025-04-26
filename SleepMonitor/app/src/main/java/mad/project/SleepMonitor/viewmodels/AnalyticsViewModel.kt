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
    private val repository: AnalyticsRepository // Инжектируйте или передавайте экземпляр
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsScreenState())
    val state: StateFlow<AnalyticsScreenState> = _state.asStateFlow()

    init {
        // Загружаем данные для начального диапазона при инициализации
        fetchReportData(TimeRange.ALL)
    }

    fun onTimeRangeSelected(newTimeRange: TimeRange) {
        if (newTimeRange != _state.value.selectedTimeRange || _state.value.report == null) {
            _state.update { it.copy(selectedTimeRange = newTimeRange) }
            fetchReportData(newTimeRange) // Вызываем обновленную функцию
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
                            report = result.data, // data здесь non-null из-за логики Success
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            // report = null, // Можно оставить старые данные при ошибке
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> {
                    // Loading обычно обрабатывается в репозитории или use-case,
                    // если они возвращают Flow. Здесь у нас suspend-функция.
                    // Но если бы Resource.Loading возвращался, обработка была бы здесь.
                    // _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}