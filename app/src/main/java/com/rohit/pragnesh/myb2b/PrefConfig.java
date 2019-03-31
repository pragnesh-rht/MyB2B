package com.rohit.pragnesh.myb2b;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file),
                Context.MODE_PRIVATE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void writeTokens(String refresh, String access) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_refresh_token), refresh);
        editor.putString(context.getString(R.string.pref_access_token), access);
        editor.apply();
    }


    public String readRefresh() {
        return sharedPreferences.getString(context.getString(R.string.pref_refresh_token), null);
    }


    public String readAccess() {
        return sharedPreferences.getString(context.getString(R.string.pref_access_token), null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.apply();
    }

    public boolean readLoginStatus() {
        return sharedPreferences.getBoolean((context.getString(R.string.pref_login_status)),
                false);
    }

    public void writeUserName(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name), username);
        editor.apply();
    }

    public String readUserName() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_name), "");
    }

    public void writePassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_password), password);
        editor.apply();
    }

    public String readPassword() {
        return sharedPreferences.getString(context.getString(R.string.pref_password), "");
    }

    public void writeUserId(int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.pref_user_id), id);
        editor.apply();
    }

    public int readUserId() {
        return sharedPreferences.getInt(context.getString(R.string.pref_user_id), 0);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void displayToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
