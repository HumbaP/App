package mx.com.sisei.www.sisei;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity2 extends AppCompatActivity {
    private static String TAG="MainActivity";
    BottomNavigationView bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomMenu=(BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        Log.d(TAG,"Profile Pressed");
                        break;
                    case R.id.event:
                        Log.d(TAG,"Event Pressed");

                        break;
                    case R.id.home:
                        Log.d(TAG,"Home Pressed");

                        break;
                    case R.id.prizes:
                        Log.d(TAG,"prizes Pressed");

                        break;
                    case R.id.about:
                        Log.d(TAG,"about Pressed");

                        break;
                }
                return true;
            }
        });
    }
}
