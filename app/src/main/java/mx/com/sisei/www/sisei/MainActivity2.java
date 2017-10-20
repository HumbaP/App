package mx.com.sisei.www.sisei;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import mx.com.sisei.www.sisei.fragments.EventFragment;
import mx.com.sisei.www.sisei.fragments.HomeFragment;
import mx.com.sisei.www.sisei.fragments.MoreFragment;
import mx.com.sisei.www.sisei.fragments.MyQrFragment;
import mx.com.sisei.www.sisei.fragments.PrizesFragment;
import mx.com.sisei.www.sisei.fragments.ProfileFragment;
import mx.com.sisei.www.sisei.fragments.RankFragment;
import mx.com.sisei.www.sisei.listeners.MCallback;

public class MainActivity2 extends AppCompatActivity implements MCallback{
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
                        Fragment profile= ProfileFragment.newInstance(MainActivity2.this);
                        changeFragment(profile,"Profile1");
                        Log.d(TAG,"Profile Pressed");

                        break;
                    case R.id.event:

                        Fragment event= EventFragment.newInstance(MainActivity2.this);
                        changeFragment(event,"Event1");
                        Log.d(TAG,"Event Pressed");

                        break;
                    case R.id.home:

                        Fragment home= HomeFragment.newInstance(MainActivity2.this);
                        changeFragment(home,"Home1");
                        Log.d(TAG,"Home Pressed");

                        break;
                    case R.id.prizes:

                        Fragment prizes= PrizesFragment.newInstance(MainActivity2.this);
                        changeFragment(prizes,"Prizes1");
                        Log.d(TAG,"prizes Pressed");

                        break;
                    case R.id.about:

                        Fragment about= MoreFragment.newInstance(MainActivity2.this);
                        changeFragment(about,"More1");
                        Log.d(TAG,"about Pressed");

                        break;
                }
                return true;
            }
        });
        if(ACTIVE_FRAME==2){
             bottomMenu.getMenu().getItem(2).setChecked(true);
        }
        Fragment home= HomeFragment.newInstance(this);
        changeFragment(home,"Home1");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_button:
                this.onBackPressed();
                break;
            default:
                Log.d(TAG,"Something happened");
        }

        return true;
    }

    @Override
    public void addFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out).add(R.id.frame, fragment,fragment.getTag()).addToBackStack(fragment.getTag()).commit();
        Log.d(TAG,"Fragment changed to "+fragment.getClass());

    }

    @Override
    public void removeFragment(Fragment f) {
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().remove(f).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
        return true;
    }
}

