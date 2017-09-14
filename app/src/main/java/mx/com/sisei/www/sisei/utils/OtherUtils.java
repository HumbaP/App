package mx.com.sisei.www.sisei.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import mx.com.sisei.www.sisei.R;

/**
 * Created by manni on 6/26/2017.
 */

public class OtherUtils {

    private static WeakReference<Activity> reference;

    private static final String TAG="OtherUtils";

    public static void RecompensaAlertDialog(final Activity context, JSONObject jsonObject) {
        Log.d(TAG, "");
        AlertDialog.Builder insignia=null;
        AlertDialog.Builder puntos=null;
        try{
            if(jsonObject.get("Insignia_Ganada")!=null){
                View view= context.getLayoutInflater().inflate(R.layout.dialog_rewards,null);
                final ImageView reward=((ImageView) view.findViewById(R.id.image_reward));
                insignia=new AlertDialog.Builder(context).setTitle(context.getString(R.string.alert_title_won));
                insignia.setView(view);
                ((TextView) view.findViewById(R.id.text_reward)).setText(context.getString(R.string.alert_badge_won));
                Picasso.with(context).load(jsonObject.getJSONArray("Insignia_Ganada").getJSONObject(0).getString("Icono_Path")).into(reward, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) reward.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        reward.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        reward.setImageResource(R.drawable.insigniass);
                    }
                });
            }

        }catch (JSONException j){
            Log.d(TAG, "RecompensaAlertDialog: Bagde not exists");
        }
        try{
            if(jsonObject.get("Puntos_Ganado")!=null){
                View view =context.getLayoutInflater().inflate(R.layout.dialog_rewards,null);
                ImageView reward=(ImageView) view.findViewById(R.id.image_reward);
                Picasso.with(context).load(R.drawable.puntos).into(reward);
                puntos=new AlertDialog.Builder(context).setTitle(context.getString(R.string.alert_title_won));
                puntos.setView(view);
                ((TextView) view.findViewById(R.id.text_reward)).setText(ContextSingleton.getApplicationContext().getString(R.string.alert_points_won_start)+" "+jsonObject.getJSONArray("Puntos_Ganado").getJSONObject(0).getString("Cantidad")+" "+ContextSingleton.getApplicationContext().getString(R.string.alert_points_won_end));
            }
        }catch (JSONException j){
            Log.d(TAG, "RecompensaAlertDialog: Points not exists");
        }

        if(insignia!=null){
            if(puntos!=null){
                final AlertDialog.Builder finalPuntos = puntos;
                insignia.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalPuntos.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        finalPuntos.create().show();
                    }
                }).create().show();
            }
            else{
                insignia.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                insignia.create().show();
            }
        }else if(puntos!=null){
            puntos.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            puntos.create().show();
        }
    }
    private static ProgressDialog progressDialog;
    public static void progressDialogDismiss(){
        if(progressDialog!=null){
            if(reference!=null){
                if(reference.get()!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        }
    }
    public static void progressDialogShow(Context context, String message){
        if(progressDialog!=null){
            progressDialogDismiss();
        }
        reference=new WeakReference(context);
        if(reference.get()!=null && !reference.get().isFinishing()){
            progressDialog= new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public static void showInfoDialog(Activity activity,String tittle,String message){
        reference=new WeakReference(activity);
        if(reference.get()!=null && !reference.get().isFinishing()){
            new AlertDialog.Builder(reference.get()).setTitle(tittle).setMessage(message).setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

    }

}
