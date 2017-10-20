package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.com.sisei.www.sisei.LoginActivity;
import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.adapters.ConferenceAdapter;
import mx.com.sisei.www.sisei.adapters.WorkshopAdapter;
import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.listeners.MCallback;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopFragment extends Fragment {
     private static final String TAG="WorkshopFragment";
    //Data
    JSONObject data;

    //List items
    RecyclerView list;
    WorkshopAdapter adapter;
    LinearLayoutManager layoutManager;

    MCallback callback;
    public WorkshopFragment() {
        // Required empty public constructor
    }


    public static WorkshopFragment newInstance(MCallback callback) {
        WorkshopFragment fragment = new WorkshopFragment();
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
        View outView = inflater.inflate(R.layout.fragment_workshop, container, false);
        list=(RecyclerView) outView.findViewById(R.id.list_workshops);
        layoutManager=new LinearLayoutManager(this.getContext());
        updateInfo();
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

    void updateInfo(){
        APIService apiService= APIClient.getClient(this.getString(R.string.server_blood)).create(APIService.class);
        Call<ResponseBody> call = apiService.get_workshops();
        Log.d(TAG, "getHomeFragment: "+call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject=null;
                try {
                    Log.d(TAG,call.request().url().toString());
                    jsonObject=new JSONObject(response.body().string());
                    Log.d(TAG,"Length "+jsonObject.getJSONArray("Talleres").length());
                        loadDataToView(jsonObject);
                } catch (JSONException e) {
                    //Error con el json
                    e.printStackTrace();
                } catch (IOException e) {
                    //No hubo conexion, plox agrega algo
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                OtherUtils.showInfoDialog(getActivity(),getString(R.string.alert_title_error_connection), getString(R.string.alert_error_connection));
            }
        });
    }

    private void loadDataToView(JSONObject jsonObject) {
        this.data=jsonObject;
        try {
            adapter=new WorkshopAdapter(data.getJSONArray("Talleres"),callback);
            list.setLayoutManager(layoutManager);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d(TAG,"DAta changed");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
