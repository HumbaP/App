package mx.com.sisei.www.sisei;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    
    private static final String TAG="ScannerActivity";

    public static final int INTENT_QR=19604;

    private static final String[] URLS={"www.sisei.com.mx"};

    private ZXingScannerView mScannerView;
    private String breaker="0me7";
    private List<String> trash = Arrays.asList("vWYU36b3CLNByPPSETIYWnCFpOAU0bla","TVWBD8L83oj0EDKCKQ17X4TNeMTV1NGv","6zlB0k3VvZWz4hK5sLyvOqFCgHmC1nNc","GTs0LFbX7nzR1Dm8WgoKROqqtf70OER2","lSDnYNBAdBvjYzK4aqxTbFHIQbx8idne");
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        this.mScannerView=(ZXingScannerView) findViewById(R.id.zxscan);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        findViewById(R.id.scanner_info_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtils.showInfoDialog(ScannerActivity.this,getString(R.string.info_header_qr),getString(R.string.info_qr));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        String qr=result.getText();
        if(qr!=null){
            if(Character.isDigit(qr.charAt(0))){
                if(qr.charAt(0)=='2'){
                    ArrayList<String> words = new ArrayList<String>(Arrays.asList(qr.split(breaker)));
                    ArrayList<String> deletedWords= new ArrayList<>();
                    if(words.size()==6){
                        for(String key: words){
                            if(trash.contains(key)){
                                deletedWords.add(key);
                            }
                        }
                        for(String keyToDelete: deletedWords){
                            words.remove(keyToDelete);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                OtherUtils.progressDialogShow(ScannerActivity.this,"Esperando respuesta");
                            }
                        });
                        Log.d(TAG, "handleResult: Mensaje "+words.get(0)+"\t"+words.get(1));
                        APIService apiService= APIClient.getClient(this.getString(R.string.server_blood)).create(APIService.class);
                        Call<ResponseBody> call = apiService.try_word(SavingUtils.preferences.getString(getString(R.string.shared_fbId),""),SavingUtils.preferences.getString(getString(R.string.shared_master1),""),SavingUtils.preferences.getString(getString(R.string.shared_master2),""),words.get(1));
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bundle b = new Bundle();
                                        if(response!=null){
                                            if(response.code()==200){
                                                try {
                                                    OtherUtils.progressDialogDismiss();
                                                    JSONObject jsonObject =new JSONObject(response.body().string());
                                                    Log.d(TAG, jsonObject.toString());
                                                    if(jsonObject.getString("Error").equals("ALL_OK")){
                                                        Bundle bundle= new Bundle();
                                                        bundle.putString("json",jsonObject.getJSONObject("Data").toString());
                                                        finish(Activity.RESULT_OK,bundle);
                                                    }else {
                                                        if(jsonObject.getString("Error").equals("Word_Not_Available")){
                                                            b.putString("title",getString(R.string.alert_title_scanner_available));
                                                            b.putString("message",getString(R.string.alert_scanner_available));
                                                            finish(Activity.RESULT_CANCELED,b);
                                                            return;
                                                        }
                                                        if(jsonObject.getString("Error").equals("System_OFF")){
                                                            b.putString("title",getString(R.string.alert_title_error));
                                                            b.putString("message",getString(R.string.alert_error));
                                                            finish(Activity.RESULT_CANCELED,b);
                                                        }
                                                        finish(Activity.RESULT_CANCELED,new Bundle());
                                                    }
                                                } catch (IOException e) {
                                                    b.putString("title",getString(R.string.alert_title_error));
                                                    b.putString("message",getString(R.string.alert_error));
                                                    finish(Activity.RESULT_CANCELED,b);
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    b.putString("title",getString(R.string.alert_title_error));
                                                    b.putString("message",getString(R.string.alert_error));
                                                    finish(Activity.RESULT_CANCELED,b);
                                                    e.printStackTrace();
                                                }
                                            }
                                        }else{
                                            b.putString("title",getString(R.string.alert_title_error));
                                            b.putString("message",getString(R.string.alert_error));
                                            finish(Activity.RESULT_CANCELED,b);
                                        }
                                    }
                                });


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Bundle b=new Bundle();
                                b.putString("title",getString(R.string.alert_title_error_connection));
                                b.putString("message",getString(R.string.alert_error_connection));
                                finish(Activity.RESULT_CANCELED,b);
                            }
                        });

                    }
                }else {
                    Log.d(TAG, "handleResult: Size not 6");
                    Bundle b=new Bundle();
                    b.putString("title",getString(R.string.alert_title_scanner_wrong));
                    b.putString("message",getString(R.string.alert_scanner_wrong));
                    finish(Activity.RESULT_CANCELED,b);
                }
            }else if(qr.contains("sisei.com.mx")){
                Log.d(TAG, "handleResult: Can sisei.com.mx");

               /* Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.sisei.com.mx"));
/**/
                Intent intent=new Intent(ScannerActivity.this,WebActivity.class);
                Bundle b=new Bundle();
                b.putString("url", "http://www.sisei.com.mx");
                intent.putExtras(b);
                startActivity(intent);
//                startActivity(i);
                finish();
            }else{
                Bundle b=new Bundle();
                b.putString("title",getString(R.string.alert_title_scanner_wrong));
                b.putString("message",getString(R.string.alert_scanner_wrong));
                finish(Activity.RESULT_CANCELED,b);
            }
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtils.progressDialogDismiss();
    }

    public void finish(int code, Bundle data){
        Intent returnData= new Intent();
        returnData.putExtras(data);
        Log.d(TAG, "finish: Returned data with code"+String.valueOf(code==Activity.RESULT_OK));
        setResult(code,returnData);

        super.finish();
    }

}
