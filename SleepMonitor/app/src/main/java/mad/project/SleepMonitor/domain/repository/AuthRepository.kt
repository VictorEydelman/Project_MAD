package mad.project.SleepMonitor.domain.repository

import mad.project.SleepMonitor.data.network.dto.AuthRequest
import mad.project.SleepMonitor.domain.model.User


interface AuthRepository {
    suspend fun login(authRequest: AuthRequest): Result<User>
    suspend fun register(authRequest: AuthRequest): Result<User>
    suspend fun check(token: String, storedPassword: String?): Result<User>
}