package mx.com.sisei.www.sisei.connections;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import mx.com.sisei.www.sisei.R;

/**
 * Created by manni on 6/22/2017.
 */

public class LoadInfoAsyncTask extends AsyncTask<Void, Void, Boolean> {


    private APIService service;
    Activity activity;
    ProgressBar progressBar;
    public LoadInfoAsyncTask(Activity activity) {
        super();
        this.activity=activity;
        service= APIClient.getClient(activity.getString(R.string.server_blood)).create(APIService.class);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar=(ProgressBar)activity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        return null;
    }

}
