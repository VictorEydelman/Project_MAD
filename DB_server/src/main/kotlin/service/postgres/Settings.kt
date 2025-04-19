package mad.project.service.postgres

import mad.project.SettingUser
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException
import java.time.LocalDate

@Serializable
data class Settings(
    var username: String = "", val name: String, val surname: String,
    @Contextual val birthday: LocalDate, var gender: Gender,
    val physicalCondition: Frequency, val caffeineUsage: Frequency,
    val alcoholUsage: Frequency, val alarmRecurring: Alarm?, var alarmTemporary: Alarm?,
    val bedTimeRecurring: BedTime?, var bedTimeTemporary: BedTime?)
class SettingsService(private val connection: Connection){
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
        private const val UPDATE_Temporary = "PDATE SETTINGS SET alarmTemporary = ?," +
                "bedTimeTemporary = ? WHERE USERNAME = ?"
        private const val DROP_TABLE = "DROP TABLE IF EXISTS SETTINGS"
        private const val EXIST_SETTING = "SELECT count(*) FROM SETTINGS WHERE username = ?"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(DROP_TABLE)
        statement.executeUpdate(CREATE_TABLE_SETTINGS)
    }
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
    fun save(settingUser: SettingUser): Boolean{
        var settings = settingUser.data
        settings.username = settingUser.username
        if(settingNotExist(settings.username)){
            return insert(settings)
        } else{
            return update(settings)
        }
    }
    fun insert(settings: Settings): Boolean{
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
                bedrec = BedTime.save(settings.bedTimeRecurring)
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
            alrec?.let { statement.setInt(9, it) }
            altem?.let { statement.setInt(10, it) }
            bedrec?.let { statement.setInt(11, it) }
            bedtem?.let { statement.setInt(12, it) }
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            //return false
            throw Exception(e)
        }
    }
    fun update(settings: Settings): Boolean{
        try {
            val Alarm = AlarmService(connection)
            if(settings.alarmRecurring!=null) {
                Alarm.update(settings.alarmRecurring)
            }
            if(settings.alarmTemporary!=null) {
                Alarm.update(settings.alarmTemporary)
            }
            val BedTime = BedTimeService(connection)
            if(settings.bedTimeRecurring!=null) {
                BedTime.update(settings.bedTimeRecurring)
            }
            if(settings.bedTimeTemporary!=null) {
                BedTime.update(settings.bedTimeTemporary!!)
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
                statement.setInt(8, settings.alarmRecurring.id)
            } else {
                statement.setNull(8, java.sql.Types.INTEGER) // Устанавливаем null для alarmRecurring
            }
            if (settings.alarmTemporary != null) {
                statement.setInt(9, settings.alarmTemporary!!.id)
            } else {
                statement.setNull(9, java.sql.Types.INTEGER) // Устанавливаем null для alarmTemporary
            }
            if (settings.bedTimeRecurring != null) {
                statement.setInt(10, settings.bedTimeRecurring.id)
            } else {
                statement.setNull(10, java.sql.Types.INTEGER) // Устанавливаем null для alarmRecurring
            }
            if (settings.bedTimeTemporary!= null) {
                statement.setInt(11, settings.bedTimeTemporary!!.id)
            } else {
                statement.setNull(11, java.sql.Types.INTEGER) // Устанавливаем null для alarmTemporary
            }
            statement.setString(12,settings.username)
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            return false
            throw Exception(e)
        }
    }

    fun get(username: String): Settings{
        val statement = connection.prepareStatement(SELECT_SETTING_BY_USERNAME)
        statement.setString(1,username);
        val result = statement.executeQuery()

        if(result.next()){
            val user = result.getString("USERNAME")
            val name = result.getString("NAME")
            val surname = result.getString("SURNAME")
            val birthday = result.getObject("BIRTHDAY") as? Date ?: throw Exception("Неверный формат даты")
            val gender = result.getString("GENDER")?.let { Gender.valueOf(it) }
                ?: throw Exception("Неверное значение для пола")
            val physicalCondition = result.getString("PHYSICALCONDITION")?.let { Frequency.valueOf(it) }
                ?: throw Exception("Неверное значение для физического состояния")
            val caffeineUsage = result.getString("CaffeineUsage")?.let { Frequency.valueOf(it) }
                ?: throw Exception("Неверное значение для употребления кофеина")
            val alcoholUsage = result.getString("AlcoholUsage")?.let { Frequency.valueOf(it) }
                ?: throw Exception("Неверное значение для употребления алкоголя")
            val alrec_id = result.getInt("alarmRecurring")
            val altem_id = result.getInt("alarmTemporary")
            val Alarm = AlarmService(connection)
            var alrec: Alarm? = null
            if(alrec_id != 0) {
                alrec = Alarm.get(alrec_id)
            }
            var altem: Alarm? = null
            if(altem_id != 0) {
                altem = Alarm.get(altem_id)
            }
            val bedrec_id = result.getInt("bedTimeRecurring")
            val bedtem_id = result.getInt("bedTimeTemporary")
            val BedTime = BedTimeService(connection)
            var bedrec: BedTime? = null
            if(bedrec_id != 0) {
                bedrec = BedTime.get(bedrec_id)
            }
            var bedtem: BedTime? = null
            if(bedtem_id != 0) {
                bedtem = BedTime.get(bedtem_id)
            }
            return Settings(user, name, surname, birthday.toLocalDate(), gender, physicalCondition, caffeineUsage, alcoholUsage, alrec, altem, bedrec, bedtem)
        } else{
            throw Exception("Нету настроек")
        }
    }

    fun temporaryToNull(username: String): Boolean{
        try {
            val statement = connection.prepareStatement(UPDATE_Temporary)
            statement.setNull(1, java.sql.Types.INTEGER)
            statement.setNull(2, java.sql.Types.INTEGER)
            statement.setString(3, username)
            return true
        } catch (e: SQLException){
            return false
            throw Exception(e)
        }
    }
}