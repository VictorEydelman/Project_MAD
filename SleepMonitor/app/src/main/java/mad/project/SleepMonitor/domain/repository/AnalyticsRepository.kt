package mad.project.SleepMonitor.domain.repository

import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.domain.model.TimePreference
import mad.project.SleepMonitor.util.Resource


interface AnalyticsRepository {
    suspend fun getDailyReport(): Resource<Report>
    suspend fun getWeeklyReport(): Resource<Report>
    suspend fun getAllTimeReport(): Resource<Report>
    suspend fun getRecommendedTimes(): Resource<TimePreference>
}