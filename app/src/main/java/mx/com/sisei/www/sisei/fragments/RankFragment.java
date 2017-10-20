package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.com.sisei.www.sisei.MainActivity2;
import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.adapters.RankAdapter;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankFragment extends Fragment {

    private static final String TAG="RankFragment";

    private OnFragmentInteractionListener mListener;

    private JSONObject ranks;
    private RecyclerView ranklist;
    private RecyclerView.LayoutManager layoutManager;
    private RankAdapter adapter;


    public RankFragment() {
        // Required empty public constructor
    }
    public static RankFragment newInstance() {
        RankFragment fragment = new RankFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View outView = inflater.inflate(R.layout.fragment_rank, container, false);
        outView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.backgroundColor, null));
        ranklist=(RecyclerView) outView.findViewById(R.id.rank_list);
        layoutManager=new LinearLayoutManager(this.getContext());
        APIService service = APIClient.getClient(getString(R.string.server_blood)).create(APIService.class);
        Call<ResponseBody> call = service.rank_status();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ranks=new JSONObject(response.body().string());
                    if(ranks.getString("Error").equals("ALL_OK"))
                       adapter=new RankAdapter(ranks.getJSONArray("Data"));
                    else{
                        Toast.makeText(getActivity(),"Error con el servidor, estamos trabajando para arreglar este fallo.",Toast.LENGTH_LONG);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),"Error con el servidor, estamos trabajando para arreglar este fallo.",Toast.LENGTH_LONG);
                    getActivity().getSupportFragmentManager().popBackStack();
                    e.printStackTrace();
                } catch (IOException e) {
                    OtherUtils.showInfoDialog(getActivity(),getString(R.string.alert_title_error_connection), getString(R.string.alert_error_connection));                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    e.printStackTrace();

                }
                ranklist.setLayoutManager(layoutManager);
                ranklist.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_LONG);
                getActivity().getSupportFragmentManager().popBackStack();
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
        Toolbar tb=(Toolbar)getActivity().findViewById(R.id.toolbar);
        tb.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm= getActivity().getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                EventFragment f=EventFragment.newInstance((MainActivity2)getActivity());
                ft.add(f, "Event");
                ft.commit();

            }
        });

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
