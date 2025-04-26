package mad.project
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mad.project.service.postgres.Alarm
import mad.project.service.postgres.BedTime
import mad.project.service.postgres.Frequency
import mad.project.service.postgres.Gender
import mad.project.service.postgres.Settings
import java.time.LocalDate

@Serializable
data class DataUser<T> (val username: String, val data: T)
@Serializable
data class SettingWithOutUser(val name: String, val surname: String,
                              @Contextual val birthday: LocalDate, var gender: Gender,
                              val physicalCondition: Frequency, val caffeineUsage: Frequency,
                              val alcoholUsage: Frequency, val alarmRecurring: Alarm?, var alarmTemporary: Alarm?,
                              val bedTimeRecurring: BedTime?, var bedTimeTemporary: BedTime?)