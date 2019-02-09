package leavesc.hello.monitor.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:04
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class JsonConverter {

    private Gson gson;

    private JsonConverter() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    public static Gson getInstance() {
        return JsonConverterHolder.instance.gson;
    }

    private static class JsonConverterHolder {
        private static final JsonConverter instance = new JsonConverter();
    }

}
