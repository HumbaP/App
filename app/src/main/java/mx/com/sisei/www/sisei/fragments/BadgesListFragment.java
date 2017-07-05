package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.adapters.BadgeAdapter;

public class BadgesListFragment extends Fragment {

    JSONObject jsonObject;
    private static final String TAG="BadgesListFragment";
    private OnFragmentInteractionListener mListener;
    private RecyclerView badgesList;
    RecyclerView.Adapter badgesListAdapter;
    RecyclerView.LayoutManager badgesListManager;


    public BadgesListFragment() {
        // Required empty public constructor
    }

    public static BadgesListFragment newInstance(JSONObject jsonObject) {
        BadgesListFragment fragment = new BadgesListFragment();
        fragment.jsonObject=jsonObject;
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
        View outView = inflater.inflate(R.layout.fragment_badges_list, container, false);
        badgesList=(RecyclerView) outView.findViewById(R.id.list_badges);
        //Use linear layout manager
        badgesListManager=new GridLayoutManager(this.getContext(),2);


        badgesList.setHasFixedSize(true);
        badgesList.setLayoutManager(badgesListManager);
        //adapter
        try {

            badgesListAdapter= new BadgeAdapter(jsonObject.getJSONArray("Insignias"));
            badgesList.setAdapter(badgesListAdapter);
            Log.d(TAG, "onCreateView: items "+badgesListAdapter.getItemCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outView;
    }


    public void loadData(JSONObject jsonIn){


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
