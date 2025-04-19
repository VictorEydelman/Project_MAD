package mad.project
import kotlinx.serialization.Serializable
import mad.project.service.postgres.Settings

@Serializable
data class SettingUser (val username: String, val data: Settings)