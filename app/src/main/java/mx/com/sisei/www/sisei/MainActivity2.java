package mx.com.sisei.www.sisei;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import mx.com.sisei.www.sisei.fragments.RankFragment;

public class MainActivity2 extends AppCompatActivity {
    private static String TAG="MainActivity";
    BottomNavigationView bottomMenu;
    private Toolbar toolbar;

    private Handler handler;

    //Menu variables for functioning
    public static int ACTIVE_FRAME=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        handler=new Handler();
        //Set the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Get bottom menu
        bottomMenu=(BottomNavigationView) findViewById(R.id.bottom_menu);
        //Actions of bottom menu
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        Fragment rank= RankFragment.newInstance();
                        changeFragment(rank,"Perfil");
                        Log.d(TAG,"Profile Pressed");

                        break;
                    case R.id.event:

                        Fragment event= RankFragment.newInstance();
                        changeFragment(event,"Evento");
                        Log.d(TAG,"Event Pressed");

                        break;
                    case R.id.home:

                        Fragment home= RankFragment.newInstance();
                        changeFragment(home,"Inicio");
                        Log.d(TAG,"Home Pressed");

                        break;
                    case R.id.prizes:

                        Fragment prizes= RankFragment.newInstance();
                        changeFragment(prizes,"Premios");
                        Log.d(TAG,"prizes Pressed");

                        break;
                    case R.id.about:

                        Fragment about= RankFragment.newInstance();
                        changeFragment(about,"Mas");
                        Log.d(TAG,"about Pressed");

                        break;
                }
                return true;
            }
        });
        if(ACTIVE_FRAME==2){
             bottomMenu.getMenu().getItem(2).setChecked(true);
        }
    }

    void changeFragment(final Fragment toChange, final String tag){
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, toChange, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            handler.post(mPendingRunnable);
        }

    }

}
