package com.example.taskmanager.classes;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class SharedPrefsUtil {

    private SharedPreferences mSharedPreferences;
    private static SharedPrefsUtil instance;
    private Gson gson;

    public static SharedPrefsUtil getInstance(Context context){
        if(instance == null){
            instance = new SharedPrefsUtil(context);
        }
        return instance;
    }
    private SharedPrefsUtil(Context context) {
        this.mSharedPreferences = context.getSharedPreferences("APP_SESSION", MODE_PRIVATE);
        this.gson =  new GsonBuilder()
                .create();
    }

    public boolean contains(String key){
       return mSharedPreferences.contains(key);
    }

    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public <T> T get(String key, Class<T> clazz, T defaultValue){
        String json  = get(key, null);
        if (!TextUtils.isEmpty(json)){
            return gson.fromJson(json, clazz);
        } else {
            return defaultValue;
        }
    }

    public <T> void put(String key, Class<T> clazz, T value){
        String jsonValue = gson.toJson(value);
        put(key, jsonValue);
    }

    public Map<String,?> getAll(){
        return mSharedPreferences.getAll();
    }

    // https://riptutorial.com/android/example/4983/commit-vs--apply
    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).commit();
    }
}

