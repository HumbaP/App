package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.MainActivity;
import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.WebActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    JSONObject jsonObject;

    private static final String TAG="ProfileFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Components
    ImageView picturProfile;
    TextView txtName,txtCarnet, txtBadges,txtPoints;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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

    }
    boolean shouldShow=false;
    View outView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View outView = inflater.inflate(R.layout.fragment_profile, container, false);
        this.outView=outView;
        //Initialize components
        picturProfile= (ImageView)outView.findViewById(R.id.profile_image);
        txtName=(TextView) outView.findViewById(R.id.profile_name);
        txtCarnet=(TextView) outView.findViewById(R.id.carnet_type);
        outView.findViewById(R.id.profile_badges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadHomeOnBackPress=true;
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
                ((MainActivity) getActivity()).loadHomeOnBackPress=true;
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).add(R.id.frame,PointsListFragment.newInstance(jsonObject),"Points").addToBackStack("Points").commit();

            }
        });
        outView.findViewById(R.id.dinamicas_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),WebActivity.class);
                Bundle b=new Bundle();
                b.putString("url", "https://twitter.com/SiseiOnline");
                intent.putExtras(b);
                startActivity(intent);
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


        return outView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
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
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
