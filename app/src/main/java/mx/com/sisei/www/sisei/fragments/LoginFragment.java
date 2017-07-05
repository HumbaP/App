package mx.com.sisei.www.sisei.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.LoginUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private static final String TAG="LoginFragment";

    LoginButton loginButton;
    CallbackManager callbackManager;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
        final APIService apiService= APIClient.getClient(getString(R.string.server_blood)).create(APIService.class);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String fbId= loginResult.getAccessToken().getUserId();
                AccessToken fbToken = loginResult.getAccessToken();
                int token1= LoginUtil.generateKeyOne(fbId);
                int token2= LoginUtil.generateKeyTwo(fbId);
                Log.d("LoginFragment","Creadas las keys");

                    Profile profile= Profile.getCurrentProfile();
                    Log.d(TAG , profile.getFirstName());
                    Log.d(TAG, profile.getLastName());
                    //Llamamos al servidor con la informacion de fb
                    Call<ResponseBody> auth = apiService.doAuth(fbId,profile.getName(), profile.getFirstName(),profile.getLinkUri().getEncodedPath(),token1+"",token2+"");
                    auth.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            Log.d("LoginFragment","Entro correctamente");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });


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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
