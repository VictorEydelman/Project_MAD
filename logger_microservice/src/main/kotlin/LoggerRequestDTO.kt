import io.github.oshai.kotlinlogging.Level

data class LoggerRequestDTO(
    val name: String,
    val message: String,
    val level: Level,
    val stacktrace: String?,
)
