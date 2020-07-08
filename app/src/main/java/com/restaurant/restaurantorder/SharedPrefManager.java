package com.restaurant.restaurantorder;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static SharedPreferences mSharedPreferences;
    private static Context mCtx;
    private static final String pref_name = "mysharedpref12";


    private SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public static boolean setUsername(String username, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("username", username);
        return editor.commit();
    }

    public static String getUsername(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("username", null);
    }



//    public static boolean setEmail(String email, Context mContext) {
//        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString("email", email);
//        return editor.commit();
//    }

    public static String getEmail(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("email", null);
    }

    public static boolean setEmail(String email, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("email", email);
        return editor.commit();
    }

    public static boolean setPhone(String phone, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("phone", phone);
        return editor.commit();
    }

    public static String getPhone(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("phone", null);
    }


    public static boolean setToken(String token, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("token", token);
        return editor.commit();
    }

    public static String getToken(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("token", null);
    }

    public static boolean setType(String type, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("type", type);
        return editor.commit();
    }

    public static String getType(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("type", null);
    }


    public static boolean setID(String ID, Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString("userid", ID);
        return editor.commit();
    }

    public static String getID(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(pref_name, 0);
        return mSharedPreferences.getString("userid", "");
    }

}
