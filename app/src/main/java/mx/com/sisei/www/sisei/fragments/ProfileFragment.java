package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.com.sisei.www.sisei.LoginActivity;
import mx.com.sisei.www.sisei.MainActivity;
import mx.com.sisei.www.sisei.MainActivity2;
import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.WebActivity;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.listeners.MCallback;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    JSONObject jsonObject;

    private static final String TAG="ProfileFragment";

    //Components
    ImageView picturProfile;
    TextView txtName,txtCarnet, txtBadges,txtPoints;

    MCallback callback;
    public ProfileFragment() {

    }


    public static ProfileFragment newInstance(MCallback callback) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.callback=callback;
        return fragment;
    }


    public static ProfileFragment newInstance(JSONObject object) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.jsonObject=object;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateProfile();

    }
    boolean shouldShow=false;
    View outView;
    SwipeRefreshLayout swipe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View outView = inflater.inflate(R.layout.fragment_profile, container, false);
        this.outView=outView;
        swipe=(SwipeRefreshLayout) outView.findViewById(R.id.swipe);

        //Initialize components
        picturProfile= (ImageView)outView.findViewById(R.id.profile_image);
        txtName=(TextView) outView.findViewById(R.id.profile_name);
        txtCarnet=(TextView) outView.findViewById(R.id.carnet_type);
        outView.findViewById(R.id.profile_badges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity2) getActivity()).loadHomeOnBackPress=true;
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).add(R.id.frame,BadgesListFragment.newInstance(jsonObject),"Badges").addToBackStack("Badges").commit();
                    getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                            shouldShow=true;
                        }
                    });
            }
        });
        outView.findViewById(R.id.profile_points).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ((MainActivity) getActivity()).loadHomeOnBackPress=true;
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).add(R.id.frame,PointsListFragment.newInstance(jsonObject),"Points").addToBackStack("Points").commit();

            }
        });
        outView.findViewById(R.id.dinamicas_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadFragment();/*
                Intent intent=new Intent(getActivity(),WebActivity.class);
                Bundle b=new Bundle();
                b.putString("url", "https://twitter.com/SiseiOnline");
                intent.putExtras(b);
                startActivity(intent);*/
            }
        });
        txtBadges=(TextView) outView.findViewById(R.id.profile_badges_count);
        txtPoints=(TextView) outView.findViewById(R.id.profile_points_count);
        outView.setVisibility(View.GONE);
        outView.findViewById(R.id.my_qr_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).add(R.id.frame,MyQrFragment.newInstance(),"MyQr").addToBackStack("MyQr").commit();
            }
        });


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateProfile();
                swipe.setRefreshing(false);
            }
        });

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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        if(shouldShow)
            outView.setVisibility(View.VISIBLE);

    }



    public void loadDataToView(JSONObject jsonIn){
        try {
            this.jsonObject=jsonIn.getJSONObject("Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Loading info to UI
        Picasso.with(getContext()).load(Profile.getCurrentProfile().getProfilePictureUri(120,120)).into(picturProfile, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) picturProfile.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                picturProfile.setImageDrawable(imageDrawable);
            }
            @Override
            public void onError() {
                picturProfile.setImageResource(R.drawable.bg_circle);
            }
        });

        try {
            txtName.setText(jsonObject.getString("FB_Name"));
            txtCarnet.setText(jsonObject.getString("Carnet"));
            if(jsonObject.getString("Total_Puntos")==null){
                txtPoints.setText("0");
            }
            else txtPoints.setText(jsonObject.getString("Total_Puntos"));
            if(jsonObject.getString("Insignias")==null){
                txtBadges.setText("0");
            }
            else
                txtBadges.setText(jsonObject.getJSONArray("Insignias").length()+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getView().setVisibility(View.VISIBLE);
        Log.d(TAG, "loadDataToView: Profile info loaded successful");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean status=false;
    public boolean updateProfile(){
        status=false;
        APIService apiService= APIClient.getClient(this.getString(R.string.server_blood)).create(APIService.class);
        Call<ResponseBody> call = apiService.me(SavingUtils.preferences.getString(getString(R.string.shared_fbId),""),SavingUtils.preferences.getString(getString(R.string.shared_master1),""),SavingUtils.preferences.getString(getString(R.string.shared_master2),""));
        Log.d(TAG, "getHomeFragment: "+call.request().url().toString());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getString("Error").equals("ALL_OK")){
                        loadDataToView(jsonObject);
                        status=true;
                    }else{
                        SavingUtils.logOut();
                        //Chido aqui, podemos continuar
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        status=false;
                    }
                } catch (JSONException e) {
                    //Error con el json
                    e.printStackTrace();
                } catch (IOException e) {
                    //No hubo conexion, plox agrega algo
                    e.printStackTrace();
                }
                getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                OtherUtils.showInfoDialog(getActivity(),getString(R.string.alert_title_error_connection), getString(R.string.alert_error_connection));

            }
        });

        return status;
    }

    void reloadFragment(){
        Fragment newFragment = new ProfileFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame, newFragment).commit();

    }

}
