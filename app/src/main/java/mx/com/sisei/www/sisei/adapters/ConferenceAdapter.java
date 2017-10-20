package mx.com.sisei.www.sisei.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.fragments.ConferenceFragment;
import mx.com.sisei.www.sisei.listeners.MCallback;
import mx.com.sisei.www.sisei.listeners.OnItemClickListener;
import mx.com.sisei.www.sisei.utils.ContextSingleton;

/**
 * Created by manni on 10/17/2017.
 */

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ViewHolder> implements OnItemClickListener{

    private static final String TAG="ConferenceAdapter";
    JSONArray conferences;
    MCallback callback;
    public ConferenceAdapter(JSONArray conferences, MCallback callback){
        this.conferences=conferences;
        this.callback=callback;
    }

    @Override
    public ConferenceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View outview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conference_list,parent,false);
        //to do here
        Log.d(TAG, "onCreateViewHolder: Created");
        ViewHolder viewHolder=new ViewHolder(outview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            Picasso.with(ContextSingleton.getApplicationContext()).load(conferences.getJSONObject(position).getString("Icono_Path")).into(holder.image, new Callback() {
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
            holder.conferenceNamel.setText(conferences.getJSONObject(position).getString("Name"));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        onItemClick(conferences.getJSONObject(position));
                    } catch (JSONException e) {
                        //Un error ha ocurrido con la lista
                        Log.d(TAG,"Error JSON in position "+position+". JSONArray length "+conferences.length());
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
        return conferences.length();
    }

    @Override
    public void onItemClick(JSONObject item) {
        ConferenceFragment fragment = ConferenceFragment.newInstance(item,callback);
        callback.addFragment(fragment);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView conferenceNamel;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.item_cnoference_logo);
            conferenceNamel = (TextView)itemView.findViewById(R.id.item_conference_name);
            cardView =(CardView) itemView.findViewById(R.id.card_conference);
        }
    }

}
