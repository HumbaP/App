package mx.com.sisei.www.sisei;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.ContextSingleton;
import mx.com.sisei.www.sisei.utils.LoginUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG="SplashActivity";
    private static final long SPLASH_SCREEN_DURATION=3000;//Duration of the splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextSingleton.startContext(getApplicationContext());
        //Orientacion vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Esconder la barra superior
        setContentView(R.layout.activity_splash);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView) findViewById(R.id.version)).setText(version+"");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final APIService apiService= APIClient.getClient(getString(R.string.server_blood)).create(APIService.class);
        Call<ResponseBody> call= apiService.getTimeSource();
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());

        ((ImageView) findViewById(R.id.main_icon)).startAnimation(rotate);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,response.code()+"");
                Log.d(TAG,response.message());
                try {
                    //Log.d(TAG, response.body().)
                    LoginUtil.depurarArray(new JSONArray(response.body().string()));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Siguiente actividad
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        /*TimerTask task= new TimerTask() {
            @Override
            public void run() {
                //Siguiente actividad
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        };
        Timer timer  =new Timer();
        timer.schedule(task, SPLASH_SCREEN_DURATION);*/
    }
}
