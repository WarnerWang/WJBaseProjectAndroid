package com.hx.wjbaseproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hx.wjbaseproject.App;

/**
 * Created by color on 15/10/16.
 */
public class SpUtils {
    private static final String TAG = SpUtils.class.getSimpleName();
    private static SpUtils spUtils;
    private static SharedPreferences spf;

    private SpUtils() {
        spf = App.ins().getSharedPreferences("vinci", Context.MODE_PRIVATE);
    }

    public static SpUtils getInstance() {
        if (null == spUtils) {
            spUtils = new SpUtils();
        }
        return spUtils;
    }

    /**
     * 通过 instanceof 来判断数据类型再强转成对应的数据类型进行存储
     * @param key
     * @param obj
     */
    public static void put(String key, Object obj){
        SharedPreferences.Editor editor = spf.edit();

        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        }else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        }else if (obj instanceof Integer) {
            editor.putInt(key, (int) obj);
        }else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        }else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        }

        editor.apply();
    }

    /**
     * 通过instanceof 来判断数据类型再进行取值然后再进行类型转换，这种方法比较麻烦取出值之后在应用的地方还要进行类型转换
     * @param key
     * @param obj
     * @return
     */
    public static Object get(String key, Object obj){
        if (obj instanceof String) {
            return spf.getString(key, (String) obj);
        }else if (obj instanceof String) {
            return spf.getLong(key, (Long) obj);
        }else if (obj instanceof Integer) {
            return spf.getInt(key, (int) obj);
        }else if (obj instanceof Boolean) {
            return spf.getBoolean(key, (Boolean) obj);
        }else if (obj instanceof Float) {
            return spf.getFloat(key, (Float) obj);
        }

        return null;
    }

    /**
     * 全部清除文件中的内容
     */
    public static void clearSpf(){
        SharedPreferences.Editor editor = spf.edit();
        editor.clear().commit();
    }

    /**
     * 清除指定key 的内容
     * @param key
     */
    public static void remove(String key){
        SharedPreferences.Editor editor = spf.edit();
        editor.remove(key).commit();
    }


    /**
     * 获取String值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        return spf.getString(key, defValue);
    }

    /**
     * 获取int值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(String key, int defValue) {
        return spf.getInt(key, defValue);
    }

    /**
     * 获取long值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(String key, long defValue) {
        return spf.getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(String key, float defValue) {
        return spf.getFloat(key, defValue);
    }

    /**
     * 获取bo0lean值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return spf.getBoolean(key, defValue);
    }

}
