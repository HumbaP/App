package mx.com.sisei.www.sisei;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DecoderActivity extends AppCompatActivity {


    private static final String TAG="DecoderActivity";
    private TextView textDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        textDecoder = (TextView) findViewById(R.id.decoder_input);
        findViewById(R.id.decoder_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = "";
                OtherUtils.progressDialogShow(DecoderActivity.this,"Esperando respuesta");
                if(textDecoder.getText()!=null && textDecoder.getText().length()>1){
                    word=textDecoder.getText().toString();/*
                    if(!word.matches ("\\w+\\.?")){
                        //Some code here
                        return;
                    }*/
                    }else{
                        //Some stuff here
                        return;
                    }
                APIService apiService= APIClient.getClient(getString(R.string.server_blood)).create(APIService.class);
                Call<ResponseBody> call = apiService.try_word(SavingUtils.preferences.getString(getString(R.string.shared_fbId),""),SavingUtils.preferences.getString(getString(R.string.shared_master1),""),SavingUtils.preferences.getString(getString(R.string.shared_master2),""), URLEncoder.encode(word));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response!=null){
                                    if(response.code()==200){
                                        try {
                                            OtherUtils.progressDialogDismiss();
                                            JSONObject jsonObject =new JSONObject(response.body().string());
                                            Log.d(TAG, jsonObject.toString());
                                            textDecoder.setText("");
                                            if(jsonObject.getString("Error").equals("ALL_OK")){
                                                OtherUtils.RecompensaAlertDialog(DecoderActivity.this,jsonObject.getJSONObject("Data"));
                                            }else if(jsonObject.getString("Error").equals("Sesion_Error")){
                                                SavingUtils.logOut();
                                                finish();
                                            }
                                            else{
                                                if(jsonObject.getString("Error").equals("Word_Already_Taken")){
                                                    new AlertDialog.Builder(DecoderActivity.this).setTitle(getString(R.string.alert_title_word_taken)).setMessage(getString(R.string.dialog_word_already_taken)).setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    }).create().show();
                                                }else if(jsonObject.getString("Error").equals("Word_Not_Available") ){
                                                    new AlertDialog.Builder(DecoderActivity.this).setTitle(getString(R.string.alert_title_word_taken)).setMessage(getString(R.string.dialog_word_not_available)).setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    }).create().show();
                                                    return;
                                                }
                                                else if(jsonObject.getString("Error").equals("System_OFF")){

                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else{
                                    OtherUtils.showInfoDialog(DecoderActivity.this,getString(R.string.alert_title_error_connection), getString(R.string.alert_error_connection));

                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        findViewById(R.id.decoder_info_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtils.showInfoDialog(DecoderActivity.this,getString(R.string.info_header_decoder), getString(R.string.info_decoder));
            }
        });
    }
}
