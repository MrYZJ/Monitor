package leavesc.hello.sample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import leavesc.hello.sample.database.entity.MonitorHttpInformation;
import leavesc.hello.sample.utils.Converters;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:03
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
@Database(entities = {MonitorHttpInformation.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MonitorHttpInformationDatabase extends RoomDatabase {

    private static final String DB_NAME = "MonitorHttpInformation.db";

    private static volatile MonitorHttpInformationDatabase instance;

    public static MonitorHttpInformationDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MonitorHttpInformationDatabase.class) {
                if (instance == null) {
                    instance = create(context);
                }
            }
        }
        return instance;
    }

    private static MonitorHttpInformationDatabase create(final Context context) {
        return Room.databaseBuilder(context, MonitorHttpInformationDatabase.class, DB_NAME).build();
    }

    public abstract MonitorHttpInformationDao getHttpInformationDao();

}