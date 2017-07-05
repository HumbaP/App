package mx.com.sisei.www.sisei.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by manuel on 9/05/17.
 */

public class LoginUtil {
    private static int[] datajson= new int[5];
    //Regresa el numero secreto de la api
    public static void depurarArray(JSONArray array) throws JSONException {
        int valorRandom=array.getInt(0);
        int valorRandom2=array.getInt(1)-valorRandom;

        datajson[0]= array.getInt(2)-valorRandom-(valorRandom2*2);//hora
        datajson[1]= array.getInt(3)-valorRandom-(valorRandom2*3);//dia
        datajson[2]= array.getInt(4)-valorRandom-(valorRandom2*4);//mes
        datajson[3]= array.getInt(5)-valorRandom-(valorRandom2*5);//Num_secret
        datajson[4]= array.getInt(array.length()-1)-valorRandom-valorRandom2;//status

    }

    public static int generateKeyOne(String fbId){
        int code = Integer.parseInt(fbId.substring(fbId.length()-5,fbId.length()));
        Log.d("Cpde", code+"");
        int result= (int)(Math.pow(datajson[0],3)+Math.pow(datajson[1],2)+Math.pow((datajson[2]+datajson[0]),2)+(code + (datajson[3]*datajson[0])));
        return result;
    }
    public static int generateKeyTwo(String fbId){
        int code = Integer.parseInt(fbId.substring(0,5));
        Log.d("Cpde", code+"");
        int result= (int)(Math.pow(datajson[0],3)+Math.pow(datajson[1],2)+Math.pow((datajson[2]+datajson[0]),2)+(code + (datajson[3]*datajson[0])));
        Log.d("algo",Math.pow(datajson[0],3)+"+"+Math.pow(datajson[1],2)+"+"+Math.pow((datajson[2]+datajson[0]),2)+"+"+(code + (datajson[3]*datajson[0])));
        return  result;
    }

    public static boolean isOnline(){
        return datajson[4]==1;
    }



}
