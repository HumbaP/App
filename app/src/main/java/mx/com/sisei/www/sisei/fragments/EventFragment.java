package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.com.sisei.www.sisei.ConferencesFragment;
import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.adapters.ConferenceAdapter;
import mx.com.sisei.www.sisei.listeners.MCallback;


public class EventFragment extends Fragment {



    public EventFragment() {
        // Required empty public constructor
    }
    MCallback mCallback;
    public static EventFragment newInstance(MCallback callback) {
        EventFragment fragment = new EventFragment();
        fragment.mCallback=callback;
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
        View outView=inflater.inflate(R.layout.fragment_event, container, false);
        outView.findViewById(R.id.button_conferences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=ConferencesFragment.newInstance(mCallback);
                mCallback.addFragment(f);

            }
        });
        outView.findViewById(R.id.button_workshops).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=WorkshopFragment.newInstance(mCallback);
              mCallback.addFragment(f);

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
    }

}
