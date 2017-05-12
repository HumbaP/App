package mx.com.sisei.www.sisei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import mx.com.sisei.www.sisei.authentication.FacebookAsyncTask;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.LoginUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG="LoginActivity";

    LoginButton loginButton;
    CallbackManager callbackManager;
    GraphRequest graphRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager= CallbackManager.Factory.create();
        //Inicializamos el login
        loginButton= (LoginButton) findViewById(R.id.login_button);
        //Definimos los permisos
        loginButton.setReadPermissions("email");
        final APIService apiService= APIClient.getClient(getString(R.string.server_blood)).create(APIService.class);
        Call<ResponseBody> call= apiService.getTimeSource();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,response.code()+"");
                Log.d(TAG,response.message());
                try {
                    try {
                        JSONArray array = new JSONArray(response.body().string());
                        LoginUtil.depurarArray(array);
                        if (!LoginUtil.isOnline()) {//Cuando no se encuentre el servidor online...
                            //Modficar por otra cosa
                            Log.d(TAG, "Login incorrecto");
                        }else{
                        try {

                            Profile profile = (new FacebookAsyncTask(LoginActivity.this).execute()).get();
                            Log.d(TAG, profile.getLinkUri().toString());
                            Log.d(TAG, profile.getFirstName());
                            Log.d(TAG, profile.getLastName());
                            String fbId = profile.getId();

                            int token1 = LoginUtil.generateKeyOne(fbId);
                            int token2 = LoginUtil.generateKeyTwo(fbId);

                            Call<ResponseBody> auth = apiService.doAuth(fbId, profile.getName(), profile.getFirstName(), URLEncoder.encode(profile.getLinkUri().toString(), "utf-8"), token1 + "", token2 + "");
                            Log.d(TAG, "URL= " + auth.request().url().toString());
                            auth.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        Log.d(TAG, "JSON= " + response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("LoginFragment", "Entro correctamente");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        Log.d(TAG,"Host= "+call.request().url().toString());
        loginButton.registerCallback( callbackManager , new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Creamos el servicio
                /*try {
                    Response<ResponseBody> response=call.execute();
                    String resp=response.body().string();
                    Log.d(TAG, resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                String fbId= loginResult.getAccessToken().getUserId();
                AccessToken fbToken = loginResult.getAccessToken();
                int token1= LoginUtil.generateKeyOne(fbId);
                int token2= LoginUtil.generateKeyTwo(fbId);
                Log.d("LoginFragment","Creadas las keys");
                try {
                    Profile profile= (new FacebookAsyncTask(LoginActivity.this).execute()).get();
                    Log.d(TAG , profile.getFirstName());
                    Log.d(TAG, profile.getLastName());
                    //Llamamos al servidor con la informacion de fb
                    Call<ResponseBody> auth = apiService.doAuth(fbId,profile.getName(), profile.getFirstName(),profile.getLinkUri().toString(),token1+"",token2+"");
                    auth.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                Log.d(TAG,"JSON= "+response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("LoginFragment","Entro correctamente");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancel() {
                Log.d("LoginFragment","Cancelado");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LoginFragment","Ocurrio error");

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
