package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.DecoderActivity;
import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.ScannerActivity;
import mx.com.sisei.www.sisei.listeners.MCallback;
import mx.com.sisei.www.sisei.utils.OtherUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrizesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrizesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrizesFragment extends Fragment {

    private static final String TAG="PrizesFragment";

    MCallback callback;
    public PrizesFragment() {
        // Required empty public constructor
    }

    public static PrizesFragment newInstance(MCallback callback) {
        PrizesFragment fragment = new PrizesFragment();
        fragment.callback=callback;
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
        View outView = inflater.inflate(R.layout.fragment_prizes, container, false);
        outView.findViewById(R.id.decoder_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PrizesFragment.this.getActivity(),DecoderActivity.class);
                startActivity(intent);
            }
        });
        outView.findViewById(R.id.scanner_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrizesFragment.this.getActivity(),ScannerActivity.class);
                startActivityForResult(intent,ScannerActivity.INTENT_QR);
            }
        });
        outView.findViewById(R.id.ranked_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).add(R.id.frame,RankFragment.newInstance(),"Ranks").addToBackStack("Ranks").commit();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode "+requestCode);
        Log.d(TAG, "onActivityResult: resultCode "+resultCode);
        Log.d(TAG, "onActivityResult: data "+String.valueOf(data!=null));
        switch (requestCode){
            case ScannerActivity.INTENT_QR:
                if(resultCode==RESULT_OK){
                    Bundle datab=data.getExtras();
                    if(datab.getString("json")!=null){
                        try {
                            Log.d(TAG, "onActivityResult: Está bien, pero algo no");
                            OtherUtils.RecompensaAlertDialog(getActivity(),new JSONObject(datab.getString("json")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else
                        Log.d(TAG, "onActivityResult: Json not exists");
                }else{
                    if(data!=null){
                        Bundle b=data.getExtras();

                    OtherUtils.showInfoDialog(getActivity(),b.getString("title"),b.getString("message"));
                    }
                }
                break;

            default:
                Log.d(TAG, "onActivityResult: Code"+requestCode);
                Log.d(TAG, "onActivityResult: Something is wrong :C");
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
}
