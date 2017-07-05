package mx.com.sisei.www.sisei;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

public class BadgesActivity extends AppCompatActivity {

    JSONObject jsonObject;
    private static final String TAG="BadgesListFragment";
    private RecyclerView badgesList;
    RecyclerView.Adapter badgesListAdapter;
    RecyclerView.LayoutManager badgesListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);
    }
}
