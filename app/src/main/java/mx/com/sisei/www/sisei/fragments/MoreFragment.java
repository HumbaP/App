package mx.com.sisei.www.sisei.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.WebActivity;
import mx.com.sisei.www.sisei.listeners.MCallback;


public class MoreFragment extends Fragment {

    public static final String TAG="MoreFragment";
    //String utilizado para saber la jerarquia del fragmento
    public static final String TAGTYPE="1";

    MCallback callback;


    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance(MCallback callback) {
        MoreFragment fragment = new MoreFragment();
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
        View outView= inflater.inflate(R.layout.fragment_more, container, false);
        outView.findViewById(R.id.button_sponsors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), WebActivity.class);
                intent.putExtra("url","http://www.sisei.com.mx/patrocinadores.php");
                startActivity(intent);
            }
        });
        return outView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

}
