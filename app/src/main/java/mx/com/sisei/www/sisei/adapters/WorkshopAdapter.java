package mx.com.sisei.www.sisei.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.fragments.WorkshopInfoFragment;
import mx.com.sisei.www.sisei.listeners.MCallback;
import mx.com.sisei.www.sisei.utils.ContextSingleton;
import mx.com.sisei.www.sisei.utils.OtherUtils;

/**
 * Created by manni on 10/17/2017.
 */

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.ViewHolder>{

    private static final String TAG="WorkshopAdapter";
    JSONArray workshops;
    MCallback callback;
    public WorkshopAdapter(JSONArray workshops,MCallback callback){
        this.workshops=workshops;
        this.callback=callback;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View outview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workshop_list,parent,false);
        Log.d(TAG, "onCreateViewHolder");
        ViewHolder viewHolder=new ViewHolder(outview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            Picasso.with(ContextSingleton.getApplicationContext()).load(workshops.getJSONObject(position).getString("Icono_Path")).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) holder.image.getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(ContextSingleton.getApplicationContext().getResources(), imageBitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                    holder.image.setImageDrawable(imageDrawable);
                }
                @Override
                public void onError() {
                    holder.image.setImageResource(R.drawable.gub_rostro);
                }
            });
            holder.itemName.setText(workshops.getJSONObject(position).getString("Name"));
            holder.join.setText(workshops.getJSONObject(position).getString("Registrados")+"/"+workshops.getJSONObject(position).getString("Limite"));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        callback.addFragment(WorkshopInfoFragment.newInstance(workshops.getJSONObject(position)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Log.d(TAG, "onBindViewHolder: Ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return workshops.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView itemName;
        Button join;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.item_workshop_logo);
            itemName= (TextView)itemView.findViewById(R.id.item_workshop_name);
            join=(Button) itemView.findViewById(R.id.bottom_join_icon);
            cardView=(CardView) itemView.findViewById(R.id.card_workshop);
        }
    }
}
