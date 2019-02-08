package leavesc.hello.sample.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:04
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}