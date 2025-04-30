package mad.project.service.postgres

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mad.project.DataUser
import mad.project.keyDB.Logger
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException
import java.time.LocalDate

@Serializable
data class Settings(
    var username: String = "", val name: String, val surname: String,
    @Contextual val birthday: LocalDate, var gender: Gender,
    val physicalCondition: Frequency, val caffeineUsage: Frequency,
    val alcoholUsage: Frequency, var alarmRecurring: Alarm?, var alarmTemporary: Alarm?,
    var bedTimeRecurring: BedTime?, var bedTimeTemporary: BedTime?)
@Serializable
data class SettingWithOutUser(val name: String, val surname: String,
                              @Contextual val birthday: LocalDate, var gender: Gender,
                              val physicalCondition: Frequency, val caffeineUsage: Frequency,
                              val alcoholUsage: Frequency, val alarmRecurring: Alarm?, var alarmTemporary: Alarm?,
                              val bedTimeRecurring: BedTime?, var bedTimeTemporary: BedTime?)

/**
 * Класс для работы с таблицей Settings в базе данных postgres
 */
class SettingsService(private val connection: Connection){
    /**
     * Запросы к таблице:
     * CREATE_TABLE_SETTINGS
     * SELECT_SETTING_BY_USERNAME
     * INSERT_SETTING
     * UPDATE_SETTING
     * UPDATE_Temporary
     * DROP_TABLE
     * EXIST_SETTING
     * DROP_TABLE
     */
    companion object {
        private const val CREATE_TABLE_SETTINGS =
            """CREATE TABLE IF NOT EXISTS SETTINGS (
                USERNAME VARCHAR(255) PRIMARY KEY REFERENCES users (username),
                NAME VARCHAR(255),
                SURNAME VARCHAR(255),
                BIRTHDAY DATE,
                GENDER gender,
                PHYSICALCONDITION frequency,
                CAFFEINEUSAGE frequency,
                ALCOHOLUSAGE frequency,
                alarmRecurring integer NULL,
                alarmTemporary integer NULL,
                bedTimeRecurring integer NULL,
                bedTimeTemporary integer NULL
            );"""
        private const val SELECT_SETTING_BY_USERNAME = "SELECT * FROM SETTINGS WHERE USERNAME = ?"
        private const val INSERT_SETTING =
            "INSERT INTO SETTINGS (USERNAME," +
                    "NAME, " +
                    "SURNAME, " +
                    "BIRTHDAY, " +
                    "GENDER, " +
                    "PHYSICALCONDITION, " +
                    "CAFFEINEUSAGE, " +
                    "ALCOHOLUSAGE, " +
                    "alarmRecurring, " +
                    "alarmTemporary, " +
                    "bedTimeRecurring, " +
                    "bedTimeTemporary) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        private const val UPDATE_SETTING =
            "UPDATE SETTINGS SET NAME = ?, " +
                    "SURNAME = ?, " +
                    "BIRTHDAY = ?, " +
                    "GENDER = ?, " +
                    "PHYSICALCONDITION = ?, " +
                    "CAFFEINEUSAGE = ?, " +
                    "ALCOHOLUSAGE = ?, " +
                    "alarmRecurring = ?, " +
                    "alarmTemporary = ?, " +
                    "bedTimeRecurring = ?, " +
                    "bedTimeTemporary = ? WHERE USERNAME = ?"
        private const val UPDATE_Temporary = "UPDATE SETTINGS SET alarmTemporary = ?," +
                "bedTimeTemporary = ? WHERE USERNAME = ?"
        private const val DROP_TABLE = "DROP TABLE IF EXISTS SETTINGS"
        private const val EXIST_SETTING = "SELECT count(*) FROM SETTINGS WHERE username = ?"
    }

    /**
     * Создание таблицы
     * Возможно ещё её удаление
     */
    init {
        val statement = connection.createStatement()
        //statement.executeUpdate(DROP_TABLE)
        statement.executeUpdate(CREATE_TABLE_SETTINGS)
    }

    /**
     * Проверяет, что setting пользователя не существует
     */
    fun settingNotExist(username: String): Boolean {
        try{
            val statement = connection.prepareStatement(EXIST_SETTING)
            statement.setString(1,username)
            val e=statement.executeQuery();
            if (e.next() && e.getInt(1) == 1) {
                return false
                //throw Exception("Пользователь с таким именем уже существует.")
            }
            return true
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
            //throw Exception("Пользователь с таким именем уже существует.")
        }
    }

    /**
     * Сохраняет или обновляет setting пользователя
     */
    suspend fun save(settingUser: DataUser<Settings>): Boolean{
        var settings = settingUser.data
        settings.username = settingUser.username
        println(settings)
        if(settingNotExist(settings.username)){
            return insert(settings)
        } else{
            return update(settings)
        }
    }

    /**
     * Добавляет setting пользователя
     */
    suspend fun insert(settings: Settings): Boolean{
        try {
            val Alarm = AlarmService(connection)
            var alrec: Int? = null
            if(settings.alarmRecurring!=null) {
                alrec = Alarm.save(settings.alarmRecurring)
            }
            var altem: Int? = null
            if(settings.alarmTemporary!=null) {
                altem = Alarm.save(settings.alarmTemporary)
            }
            val BedTime = BedTimeService(connection)
            var bedrec: Int? = null
            if(settings.bedTimeRecurring!=null) {
                bedrec = BedTime.save(settings.bedTimeRecurring!!)
            }
            var bedtem: Int? = null
            if(settings.bedTimeTemporary!=null) {
                bedtem = BedTime.save(settings.bedTimeTemporary!!)
            }
            val statement = connection.prepareStatement(INSERT_SETTING)
            statement.setString(1, settings.username)
            statement.setString(2, settings.name)
            statement.setString(3, settings.surname)
            statement.setObject(4, settings.birthday)
            statement.setObject(5, settings.gender.name,java.sql.Types.OTHER)
            statement.setObject(6, settings.physicalCondition.name,java.sql.Types.OTHER)
            statement.setObject(7, settings.caffeineUsage.name,java.sql.Types.OTHER)
            statement.setObject(8, settings.alcoholUsage.name,java.sql.Types.OTHER)

            if (alrec != null) {
                statement.setInt(9, alrec)
            } else {
                statement.setNull(9, java.sql.Types.INTEGER)
            }
            if (altem != null) {
                statement.setInt(10, altem)
            } else {
                statement.setNull(10, java.sql.Types.INTEGER)
            }
            if (bedrec != null) {
                statement.setInt(11, bedrec)
            } else {
                statement.setNull(11, java.sql.Types.INTEGER)
            }
            if (bedtem != null) {
                statement.setInt(12, bedtem)
            } else {
                statement.setNull(12, java.sql.Types.INTEGER)
            }
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            Logger.error("Error in database postgresql")
            return false
        }
    }

    /**
     * Обновляет setting пользователя
     */
    suspend fun update(settings: Settings): Boolean{
        try {
            val Alarm = AlarmService(connection)
            var alrec_id: Int? =null
            if(settings.alarmRecurring!=null) {
                if(settings.alarmRecurring!!.id==-1){
                    alrec_id = Alarm.save(settings.alarmRecurring)
                } else{
                    Alarm.update(settings.alarmRecurring)
                    alrec_id = settings.alarmRecurring!!.id
                }
            }
            var altem_id: Int? =null
            if(settings.alarmTemporary!=null) {
                if(settings.alarmTemporary!!.id==-1){
                    altem_id = Alarm.save(settings.alarmTemporary)
                } else{
                    Alarm.update(settings.alarmTemporary)
                    altem_id = settings.alarmTemporary!!.id
                }
            }
            val BedTime = BedTimeService(connection)
            var bedrec_id: Int? =null
            if(settings.bedTimeRecurring!=null) {
                if(settings.bedTimeRecurring!!.id==-1){
                    bedrec_id = BedTime.save(settings.bedTimeRecurring!!)
                } else{
                    BedTime.update(settings.bedTimeRecurring!!)
                    bedrec_id = settings.bedTimeRecurring!!.id
                }
            }
            var bedtem_id: Int? =null
            if(settings.bedTimeTemporary!=null) {
                if(settings.bedTimeTemporary!!.id==-1){
                    bedtem_id = BedTime.save(settings.bedTimeTemporary!!)
                } else{
                    BedTime.update(settings.bedTimeTemporary!!)
                    bedtem_id = settings.bedTimeTemporary!!.id
                }
            }
            val statement = connection.prepareStatement(UPDATE_SETTING)
            statement.setString(1, settings.name)
            statement.setString(2, settings.surname)
            statement.setObject(3, settings.birthday)
            statement.setObject(4, settings.gender.name,java.sql.Types.OTHER)
            statement.setObject(5,settings.physicalCondition.name,java.sql.Types.OTHER)
            statement.setObject(6,settings.caffeineUsage.name,java.sql.Types.OTHER)
            statement.setObject(7,settings.alcoholUsage.name,java.sql.Types.OTHER)
            if (settings.alarmRecurring != null) {
                statement.setInt(8, alrec_id!!)
            } else {
                statement.setNull(8, java.sql.Types.INTEGER) // Устанавливаем null для alarmRecurring
            }
            if (settings.alarmTemporary != null) {
                statement.setInt(9, altem_id!!)
            } else {
                statement.setNull(9, java.sql.Types.INTEGER) // Устанавливаем null для alarmTemporary
            }
            if (settings.bedTimeRecurring != null) {
                statement.setInt(10, bedrec_id!!)
            } else {
                statement.setNull(10, java.sql.Types.INTEGER) // Устанавливаем null для alarmRecurring
            }
            if (settings.bedTimeTemporary!= null) {
                statement.setInt(11, bedtem_id!!)
            } else {
                statement.setNull(11, java.sql.Types.INTEGER) // Устанавливаем null для alarmTemporary
            }
            statement.setString(12,settings.username)
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            Logger.error("Error in database postgresql")
            return false
        }
    }

    /**
     * Возвращает setting по username
     */
    fun get(username: String): SettingWithOutUser?{
        try {
            val statement = connection.prepareStatement(SELECT_SETTING_BY_USERNAME)
            statement.setString(1, username);
            val result = statement.executeQuery()

            if (result.next()) {
                val user = result.getString("USERNAME")
                val name = result.getString("NAME")
                val surname = result.getString("SURNAME")
                val birthday =
                    result.getObject("BIRTHDAY") as? Date ?: throw Exception("Неверный формат даты")
                val gender = result.getString("GENDER")?.let { Gender.valueOf(it) }
                    ?: throw Exception("Неверное значение для пола")
                val physicalCondition =
                    result.getString("PHYSICALCONDITION")?.let { Frequency.valueOf(it) }
                        ?: throw Exception("Неверное значение для физического состояния")
                val caffeineUsage = result.getString("CaffeineUsage")?.let { Frequency.valueOf(it) }
                    ?: throw Exception("Неверное значение для употребления кофеина")
                val alcoholUsage = result.getString("AlcoholUsage")?.let { Frequency.valueOf(it) }
                    ?: throw Exception("Неверное значение для употребления алкоголя")
                println(result)
                val alrec_id = result.getInt("alarmRecurring")
                val altem_id = result.getInt("alarmTemporary")
                println(alrec_id)
                val Alarm = AlarmService(connection)
                var alrec: Alarm? = null
                if (alrec_id != 0) {
                    alrec = Alarm.get(alrec_id)
                }
                var altem: Alarm? = null
                if (altem_id != 0) {
                    altem = Alarm.get(altem_id)
                }
                val bedrec_id = result.getInt("bedTimeRecurring")
                val bedtem_id = result.getInt("bedTimeTemporary")
                val BedTime = BedTimeService(connection)
                var bedrec: BedTime? = null
                if (bedrec_id != 0) {
                    bedrec = BedTime.get(bedrec_id)
                }
                var bedtem: BedTime? = null
                if (bedtem_id != 0) {
                    bedtem = BedTime.get(bedtem_id)
                }
                return SettingWithOutUser(
                    name,
                    surname,
                    birthday.toLocalDate(),
                    gender,
                    physicalCondition,
                    caffeineUsage,
                    alcoholUsage,
                    alrec,
                    altem,
                    bedrec,
                    bedtem
                )
            } else {
                Logger.debug("Нету настроек")
                return null
            }
        } catch (e: SQLException){
            Logger.error("Error in database postgresql")
            return null
        }
    }

    /**
     * Удаляет все временные данные о времени
     */
    fun temporaryToNull(username: String): Boolean{
        try {
            val statement = connection.prepareStatement(UPDATE_Temporary)
            statement.setNull(1, java.sql.Types.INTEGER)
            statement.setNull(2, java.sql.Types.INTEGER)
            statement.setString(3, username)
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            Logger.error("Error in database postgresql: ",e)
            return false
        }
    }
}