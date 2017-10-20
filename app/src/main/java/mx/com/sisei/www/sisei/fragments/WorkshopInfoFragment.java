package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.utils.ContextSingleton;

public class WorkshopInfoFragment extends Fragment {

    private static final String TAG="WorkshopInfoFragment";
    JSONObject data;

    public WorkshopInfoFragment() {
        // Required empty public constructor
    }

    public static WorkshopInfoFragment newInstance(JSONObject data) {
        WorkshopInfoFragment fragment = new WorkshopInfoFragment();
        fragment.data=data;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View outView=inflater.inflate(R.layout.fragment_workshop_info, container, false);
        try {
            ((TextView)outView.findViewById(R.id.workshop_name)).setText(data.getString("Name"));
            ((TextView)outView.findViewById(R.id.workshop_description)).setText(data.getString("Descripcion"));
            ((TextView)outView.findViewById(R.id.workshop_date)).setText(data.getString("Fecha"));
            ((TextView)outView.findViewById(R.id.workshop_time)).setText(data.getString("Hora"));
            ((TextView)outView.findViewById(R.id.workshop_place)).setText(data.getString("Lugar"));
            ((Button)outView.findViewById(R.id.workshop_joined)).setText(data.getString("Registrados")+"/"+data.getString("Limite"));
            outView.findViewById(R.id.workshop_background).setBackgroundColor(Color.parseColor(data.getString("Color_Primary")));
            Picasso.with(ContextSingleton.getApplicationContext()).load(data.getString("Icono_Path")).into((((ImageView)outView.findViewById(R.id.workshop_logo))), new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) ((ImageView)outView.findViewById(R.id.workshop_logo)).getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(ContextSingleton.getApplicationContext().getResources(), imageBitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                    ((ImageView) outView.findViewById(R.id.workshop_logo)).setImageDrawable(imageDrawable);
                }
                @Override
                public void onError() {
                    ((ImageView)outView.findViewById(R.id.workshop_logo)).setImageResource(R.drawable.gub_rostro);
                }
            });

        } catch (JSONException e) {
            ((ImageView)outView.findViewById(R.id.workshop_logo)).setImageResource(R.drawable.gub_rostro);
            e.printStackTrace();
        }
        return outView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
