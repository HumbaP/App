package mx.com.sisei.www.sisei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.ContextSingleton;
import mx.com.sisei.www.sisei.utils.LoginUtil;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends FragmentActivity {

    private final static String TAG="LoginActivity";

    CallbackManager callbackManager;
    Button loginButton;
    GraphRequest graphRequest;
    AccessTokenTracker tokenTracker;
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ContextSingleton.startContext(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = (Button) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken() != null && SavingUtils.isLoggedIn()) {
            Profile.fetchProfileForCurrentAccessToken();
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            finish();
        } else {
            enableLogin();
           initComps();
        }
    }

    private void initComps(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            }
        });
        findViewById(R.id.politicas_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, WebActivity.class);
                Bundle b = new Bundle();
                b.putString("url", "http://www.sisei.com.mx/politica.html");
                i.putExtras(b);
                startActivity(i);
            }
        });

 /*       LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            mProfileTracker.stopTracking();
                            Profile.setCurrentProfile(profile2);
                            doLogin();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    doLogin();
                    Log.v("facebook - profile", profile.getFirstName());
                }
            }

            /*
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            FacebookAsyncTask fbTask=new FacebookAsyncTask(LoginActivity.this,loginResult);
                            fbTask.execute();
                            try {
                                if(fbTask.get()){
                                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                        OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error_connection),getString(R.string.alert_error_connection));
                                }
                            } catch (InterruptedException e) {
                                OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error_connection),getString(R.string.alert_error_connection));
                            } catch (ExecutionException e) {
                                OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error_connection),getString(R.string.alert_error_connection));

                            }
                        }
            -/
            @Override
            public void onCancel() {
                OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error),getString(R.string.alert_error_login_cancel));
            }

            @Override
            public void onError(FacebookException error) {
                OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error),getString(R.string.alert_error_login));
            }
        });*/
    }

    private void enableLogin(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                if(Profile.getCurrentProfile()==null){
                    //Profile is not updated, update it!
                    Profile.fetchProfileForCurrentAccessToken();
                    Log.d(TAG, "onSuccess: Profile is not ok ");
                    ProfileTracker tracker= new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            if(currentProfile!=null){
                                Profile.setCurrentProfile(currentProfile);
                                doLogin();
                            }
                        }
                    };

                    if(Profile.getCurrentProfile()!=null)
                        doLogin();
                    else
                        OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error),getString(R.string.alert_error_login));
                }else{
                    //You can continue, Profile is ok
                    doLogin();
                }
            }

            @Override
            public void onCancel() {
                OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error),getString(R.string.alert_error_login_cancel));

            }

            @Override
            public void onError(FacebookException error) {
                OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error),getString(R.string.alert_error_login));

            }
        });

    }

    private void doLogin(){
        Profile profile= Profile.getCurrentProfile();
        int token1= LoginUtil.generateKeyOne(profile.getId());
        int token2= LoginUtil.generateKeyTwo(profile.getId());
        apiService= APIClient.getClient(getString(R.string.server_blood)).create(APIService.class);
        Call<ResponseBody> auth = null;
        try {
            auth = apiService.doAuth(profile.getId(),profile.getName(), profile.getFirstName(), URLEncoder.encode(profile.getLinkUri().toString(),"UTF-8"),token1+"",token2+"");
            auth.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject;
                        jsonObject= new JSONObject(response.body().string());
                        if(jsonObject.getString("Error").equals("ALL_OK")){
                            SavingUtils.saveSession(jsonObject);
                            Intent i = new Intent(LoginActivity.this,MainActivity2.class);
                            startActivity(i);
                            finish();
                        }else{
                            //Codigo cuando el servidor no está disponible
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error_connection),getString(R.string.alert_error_connection));
                    Log.d("LoginFragment","Entro correctamente");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    OtherUtils.showInfoDialog(LoginActivity.this,getString(R.string.alert_title_error_connection),getString(R.string.alert_error_connection));
                }
        });
        }catch (Exception e){

        }
    }


    void fetchProfile(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
