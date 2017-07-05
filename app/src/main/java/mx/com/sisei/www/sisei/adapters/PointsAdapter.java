package mx.com.sisei.www.sisei.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import mx.com.sisei.www.sisei.R;

/**
 * Created by manni on 6/28/2017.
 */

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private final static String TAG="PointsAdapter";
    JSONArray points;

    public PointsAdapter(JSONArray array){
        points=array;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View outView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(outView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.name.setText(points.getJSONObject(position).getString("Name_Point"));
            holder.description.setText("Puntos: "+points.getJSONObject(position).getString("Cantidad"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return points.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView description;

        public ViewHolder(View view){
            super(view);
            this.name=(TextView)view.findViewById(R.id.text_point_name);
            this.description=(TextView)view.findViewById(R.id.text_point_description);

        }


    }
}
