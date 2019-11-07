package leavesc.hello.monitor.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * 作者：leavesC
 * 时间：2019/11/7 14:46
 * 描述：
 */
@Dao
interface MonitorHttpInformationDao {

    @Insert
    fun insert(model: HttpInformation): Long

    @Update
    fun update(model: HttpInformation)

    @Query("SELECT * FROM monitor_httpInformation WHERE id =:id")
    fun queryRecordObservable(id: Long): LiveData<HttpInformation>

    @Query("SELECT * FROM monitor_httpInformation")
    fun queryAllRecord(): List<HttpInformation>

    @Query("SELECT * FROM monitor_httpInformation order by id desc limit :limit")
    fun queryAllRecordObservable(limit: Int): LiveData<List<HttpInformation>>

    @Query("SELECT * FROM monitor_httpInformation order by id desc")
    fun queryAllRecordObservable(): LiveData<List<HttpInformation>>

    @Query("DELETE FROM monitor_httpInformation")
    fun deleteAll()

}

@Database(entities = [HttpInformation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class MonitorHttpInformationDatabase : RoomDatabase() {

    abstract val httpInformationDao: MonitorHttpInformationDao

    companion object {

        private const val DB_NAME = "MonitorHttpInformation.db"

        @Volatile
        private var instance: MonitorHttpInformationDatabase? = null

        fun getInstance(context: Context): MonitorHttpInformationDatabase {
            if (instance == null) {
                synchronized(MonitorHttpInformationDatabase::class.java) {
                    if (instance == null) {
                        instance = create(context)
                    }
                }
            }
            return instance!!
        }

        private fun create(context: Context): MonitorHttpInformationDatabase {
            return Room.databaseBuilder(context, MonitorHttpInformationDatabase::class.java, DB_NAME).build()
        }
    }

}