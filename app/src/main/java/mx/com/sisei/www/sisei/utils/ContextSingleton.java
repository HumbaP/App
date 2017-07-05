package mx.com.sisei.www.sisei.utils;

import android.content.Context;

/**
 * Created by manni on 6/21/2017.
 */

public class ContextSingleton {
    private static Context applicationContext;

    public static void startContext(Context context){
        applicationContext=context;
    }
    public static Context getApplicationContext(){
        return applicationContext;
    }
}
