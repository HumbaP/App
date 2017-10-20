package mx.com.sisei.www.sisei.fragments;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.listeners.MCallback;
import mx.com.sisei.www.sisei.utils.ContextSingleton;


public class ConferenceFragment extends Fragment {


    JSONObject data;
    MCallback callback;
    public ConferenceFragment() {
        // Required empty public constructor
    }

    public static ConferenceFragment newInstance(JSONObject jsonObject, MCallback callback  ) {
        ConferenceFragment fragment = new ConferenceFragment();
        fragment.callback=callback;
        fragment.data=jsonObject;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View outView=inflater.inflate(R.layout.fragment_conference, container, false);
        try {
            ((TextView)outView.findViewById(R.id.conference_name)).setText(data.getString("Name"));
            ((TextView)outView.findViewById(R.id.conference_description)).setText(data.getString("Descripcion"));
            ((TextView)outView.findViewById(R.id.conference_date)).setText(data.getString("Fecha"));
            ((TextView)outView.findViewById(R.id.conference_time)).setText(data.getString("Hora"));
            ((TextView)outView.findViewById(R.id.conference_place)).setText(data.getString("Lugar"));
            outView.findViewById(R.id.conference_background).setBackgroundColor(Color.parseColor(data.getString("Color_Primary")));
            Picasso.with(ContextSingleton.getApplicationContext()).load(data.getString("Icono_Path")).into((((ImageView)outView.findViewById(R.id.conference_logo))), new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) ((ImageView)outView.findViewById(R.id.conference_logo)).getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(ContextSingleton.getApplicationContext().getResources(), imageBitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                    ((ImageView) outView.findViewById(R.id.conference_logo)).setImageDrawable(imageDrawable);
                }
                @Override
                public void onError() {
                    ((ImageView)outView.findViewById(R.id.conference_logo)).setImageResource(R.drawable.bg_circle);
                }
            });

        } catch (JSONException e) {
            ((ImageView)outView.findViewById(R.id.conference_logo)).setImageResource(R.drawable.bg_circle);
            e.printStackTrace();
        }
        return outView;
    }

}
