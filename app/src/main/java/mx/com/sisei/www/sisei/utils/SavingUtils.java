package mx.com.sisei.www.sisei.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.R;

/**
 * Created by manuel on 11/05/17.
 */

public class SavingUtils {

    public static SharedPreferences preferences = ContextSingleton.getApplicationContext().getSharedPreferences("SISeIStuff", Context.MODE_PRIVATE);

    public static void saveTo(){

    }

    public static void saveSession(JSONObject jsonObject) throws JSONException {
        if(preferences==null){
            preferences = ContextSingleton.getApplicationContext().getSharedPreferences("SISeIStuff", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean(ContextSingleton.getApplicationContext().getString(R.string.shared_loggedin),true);
        editor.putString(ContextSingleton.getApplicationContext().getString(R.string.shared_fbId),jsonObject.getString("Fb_Id"));
        editor.putString(ContextSingleton.getApplicationContext().getString(R.string.shared_name),jsonObject.getString("FB_Name"));
        editor.putString(ContextSingleton.getApplicationContext().getString(R.string.shared_email),jsonObject.getString("Email"));
        editor.putString(ContextSingleton.getApplicationContext().getString(R.string.shared_master1),jsonObject.getJSONObject("Masterkeys").getString("One"));
        editor.putString(ContextSingleton.getApplicationContext().getString(R.string.shared_master2),jsonObject.getJSONObject("Masterkeys").getString("Two"));
        editor.commit();
    }

    public static boolean isLoggedIn(){
        if(preferences==null){
            preferences = ContextSingleton.getApplicationContext().getSharedPreferences("SISeIStuff", Context.MODE_PRIVATE);
        }
        return preferences.getBoolean(ContextSingleton.getApplicationContext().getString(R.string.shared_loggedin),false);
    }

    public static void logOut(){
        if(preferences==null){
            preferences = ContextSingleton.getApplicationContext().getSharedPreferences("SISeIStuff", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean(ContextSingleton.getApplicationContext().getString(R.string.shared_loggedin),false);
        editor.commit();

    }

}
