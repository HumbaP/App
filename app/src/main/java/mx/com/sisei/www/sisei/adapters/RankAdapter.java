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

import static android.content.ContentValues.TAG;

/**
 * Created by manni on 6/30/2017.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {


    JSONArray ranks;

    public RankAdapter(JSONArray ranks){
        this.ranks=ranks;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View outView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_list,parent, false);
        ViewHolder holder= new ViewHolder(outView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.rank.setText((position+1)+"");
        try {
            holder.name.setText(ranks.getJSONObject(position).getString("Nombre"));
            Log.d(TAG, "onBindViewHolder: "+ranks.getJSONObject(position).getString("Fb_Id"));
            Picasso.with(ContextSingleton.getApplicationContext()).load("https://graph.facebook.com/"+ranks.getJSONObject(position).getString("Fb_Id")+"/picture?type=normal").into(holder.image, new Callback() {
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
                    holder.image.setImageResource(R.drawable.bg_circle);
                }
            });
            holder.points.setText(ranks.getJSONObject(position).getString("Puntos")+" Puntos");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return ranks.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView rank;
        ImageView image;
        TextView name;
        TextView points;

        public ViewHolder(View itemView) {
            super(itemView);
            rank=(TextView)itemView.findViewById(R.id.text_place_rank);
            image=(ImageView) itemView.findViewById(R.id.image_rank_profile);
            name=(TextView)itemView.findViewById(R.id.text_name_rank);
            points=(TextView)itemView.findViewById(R.id.text_points_rank);
        }
    }
}
