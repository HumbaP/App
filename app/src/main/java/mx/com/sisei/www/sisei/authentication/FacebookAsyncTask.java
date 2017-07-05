package mx.com.sisei.www.sisei.authentication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.LoginUtil;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by manuel on 11/05/17.
 */

public class FacebookAsyncTask extends AsyncTask<Void, Void, Boolean>{
    private final static String TAG="FacebookAsyncTask";
    Context mContext;
    APIService apiService;
    private ProfileTracker profileTracker;

    Boolean outi=null;

    public FacebookAsyncTask(Context mContext, LoginResult loginResult) {
        super();
        this.mContext=mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        apiService= APIClient.getClient(mContext.getString(R.string.server_blood)).create(APIService.class);
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);

    }

    private boolean doWithProfile(Profile profile){
        int token1= LoginUtil.generateKeyOne(profile.getId());
        int token2= LoginUtil.generateKeyTwo(profile.getId());
        Call<ResponseBody> auth = null;
        try {
            auth = apiService.doAuth(profile.getId(),profile.getName(), profile.getFirstName(), URLEncoder.encode(profile.getLinkUri().toString(),"UTF-8"),token1+"",token2+"");

            try {
                Response<ResponseBody> response=auth.execute();
                try {
                    JSONObject jsonObject;
                    jsonObject= new JSONObject(response.body().string());
                    if(jsonObject.getString("Error").equals("ALL_OK")){
                        SavingUtils.saveSession(jsonObject);
                        return true;
                    }else{
                        //Codigo cuando el servidor no está disponible
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("LoginFragment","Entro correctamente");
            } catch (IOException e) {
                //No hay conexión a internet
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Profile profile=null;
        if(Profile.getCurrentProfile()==null){
            profileTracker=new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    outi=doWithProfile(currentProfile);
                    profileTracker.stopTracking();
                }
            };
        }else {
            profile = Profile.getCurrentProfile();
            return doWithProfile(profile);
        }
        while (outi==null){
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 1000);
        }
        return outi;

    }

}
