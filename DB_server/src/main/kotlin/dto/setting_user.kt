package mad.project.dto

import kotlinx.serialization.Serializable
import mad.project.service.postgres.Settings

@Serializable
data class setting_user (val username: String, val data: Settings)