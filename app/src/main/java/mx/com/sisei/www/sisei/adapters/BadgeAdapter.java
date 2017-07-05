package mx.com.sisei.www.sisei.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.utils.ContextSingleton;

/**
 * Created by manni on 6/26/2017.
 */

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.ViewHolder>  {

    private final static String TAG="BadeAdapter";
    JSONArray badges;

    public BadgeAdapter(JSONArray badges){
        this.badges=badges;

    }

    @Override
    public BadgeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list,parent,false);
        Log.d(TAG, "onCreateViewHolder: Created");
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BadgeAdapter.ViewHolder holder, int position) {
        try {
            
            Picasso.with(ContextSingleton.getApplicationContext()).load(badges.getJSONObject(position).getString("Icono_Path")).into(holder.image, new Callback() {
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
                    holder.image.setImageResource(R.drawable.insigniass);
                }
            });
            holder.name.setText(badges.getJSONObject(position).getString("Name"));
            Log.d(TAG, "onBindViewHolder: Ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return badges.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.text_card_title);
            image=(ImageView) itemView.findViewById(R.id.image_item);
        }
    }
}
