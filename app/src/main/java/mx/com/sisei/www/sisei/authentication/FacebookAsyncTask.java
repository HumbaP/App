package mx.com.sisei.www.sisei.authentication;

import android.content.Context;
import android.os.AsyncTask;

import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;

/**
 * Created by manuel on 11/05/17.
 */

public class FacebookAsyncTask extends AsyncTask<Void, Void, Profile>{
    private final static String TAG="FacebookAsyncTask";
    Context mContext;
    LoginButton loginButton;
    public FacebookAsyncTask(Context mContext) {
        super();
        this.mContext=mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Profile aVoid) {
        super.onPostExecute(aVoid);


    }

    @Override
    protected Profile doInBackground(Void... voids) {

        Profile profile = Profile.getCurrentProfile();
        return profile;
    }
}
