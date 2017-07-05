package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.adapters.PointsAdapter;

public class PointsListFragment extends Fragment {

    private static final String TAG="PointsListFragmetn";

    private OnFragmentInteractionListener mListener;

    private JSONObject points;

    RecyclerView pointList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layout;


    public PointsListFragment() {
        // Required empty public constructor
    }

    public static PointsListFragment newInstance(JSONObject jsonArray) {
        PointsListFragment fragment = new PointsListFragment();
        Bundle args = new Bundle();
        if(jsonArray==null){
            fragment.points=new JSONObject();
        }else
            fragment.points=jsonArray;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View outView=inflater.inflate(R.layout.fragment_points_list, container, false);
        pointList=(RecyclerView) outView.findViewById(R.id.point_list);
        layout = new LinearLayoutManager(this.getContext());
        pointList.setLayoutManager(layout);
        try {
            adapter=new PointsAdapter(points.getJSONArray("Puntos"));
            pointList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
