package mx.com.sisei.www.sisei;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.com.sisei.www.sisei.connections.APIClient;
import mx.com.sisei.www.sisei.connections.APIService;
import mx.com.sisei.www.sisei.fragments.HomeFragment;
import mx.com.sisei.www.sisei.fragments.PrizesFragment;
import mx.com.sisei.www.sisei.fragments.ProfileFragment;
import mx.com.sisei.www.sisei.utils.OtherUtils;
import mx.com.sisei.www.sisei.utils.SavingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgHeader, imgProfile;
    private TextView txtName, txtEmail;
    private Toolbar toolbar;


    //TAGS  used to attach fragments
    private static final String TAG_HOME="home";
    private static final String TAG_PROFILE="profile";
    private static final String TAG_US="us";
    private static final String TAG_PRIZES="prizes";
    private static final String TAG_CONTACT="contact";
    private static final String TAG_LOGOUT="logout";
    public static String CURRENT_TAG = TAG_HOME;

    //Current nav menu Item
    private static int navIndex=0;

    //Toolbar titles to selected tabMenu item
    private  String[] activityTitles;

    public boolean loadHomeOnBackPress=false;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler=new Handler();

        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.nav_view);

        //Nav header
        navHeader= navigationView.getHeaderView(0);
        txtName= (TextView) navHeader.findViewById(R.id.name_profile);
        txtEmail= (TextView) navHeader.findViewById(R.id.email_profile);
        imgHeader = (ImageView) navHeader.findViewById(R.id.image_banner);
        imgProfile = (ImageView) navHeader.findViewById(R.id.image_profile);

        //Load toolbar titles from string resources

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        loadNavHeader();

        setUpNavigationView();



        if (savedInstanceState == null) {
            navIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        Log.d(TAG, "loaded");
    }

    public void loadNavHeader(){
        //Loading info
        txtName.setText("¡Hola "+SavingUtils.preferences.getString(getString(R.string.shared_name),"User")+"!");
        txtEmail.setText(SavingUtils.preferences.getString(getString(R.string.shared_email),"Correo"));

        //Loading Images
        Log.d(TAG, "loadNavHeader: Profile "+Profile.getCurrentProfile().getProfilePictureUri(120,120));
        Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(120,120)).into(imgProfile, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                imgProfile.setImageDrawable(imageDrawable);
            }
            @Override
            public void onError() {
                imgProfile.setImageResource(R.drawable.bg_circle);
            }
        });

    }

    public void setUpNavigationView(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
               /*     case R.id.home:
                        navIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                 */   case R.id.profile:
                        navIndex = 0;
                        CURRENT_TAG = TAG_PROFILE;

                        break;

           /*         case R.id.us:
                        navIndex = 2;
                        CURRENT_TAG = TAG_US;

                        break;
             */       case R.id.prizes:
                        navIndex = 1;
                        CURRENT_TAG = TAG_PRIZES;

                        break;
                  /*  case R.id.contact:
                        navIndex = 4;
                        CURRENT_TAG = TAG_CONTACT;

                        break;
                   */ case R.id.logout:
                        navIndex = 2;
                        CURRENT_TAG = TAG_LOGOUT;
                        SavingUtils.logOut();
                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        navIndex = 1;
                }
                //Checking if the item is in checked state or not, if not make it in check
                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                }
                item.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        //Setting the actionBarToggle to drawer layout
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
        }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){//El drawer está abierto
            drawer.closeDrawers();
            return;
        }
        //Loads when home fragment is not displayed
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }else if(loadHomeOnBackPress){
            if(navIndex!=0){
                navIndex=0;
                return;
            }
        }

        super.onBackPressed();

    }

    public void setToolbarTitle(){
        getSupportActionBar().setTitle(activityTitles[navIndex]);
    }

    public void selectNavMenu(){
        navigationView.getMenu().getItem(navIndex).setChecked(true);
    }
    Fragment outfrag;
    public Fragment getHomeFragment(){
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        switch (navIndex){
           /* case 0:
                outfrag = new HomeFragment();
                return outfrag;
            */case 0:
                loadHomeOnBackPress=false;
                outfrag=new ProfileFragment().newInstance();
                APIService apiService= APIClient.getClient(this.getString(R.string.server_blood)).create(APIService.class);
                Call<ResponseBody> call = apiService.me(SavingUtils.preferences.getString(getString(R.string.shared_fbId),""),SavingUtils.preferences.getString(getString(R.string.shared_master1),""),SavingUtils.preferences.getString(getString(R.string.shared_master2),""));
                Log.d(TAG, "getHomeFragment: "+call.request().url().toString());
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=new JSONObject(response.body().string());
                            if(jsonObject.getString("Error").equals("ALL_OK")){
                                ((ProfileFragment) outfrag).loadDataToView(jsonObject);
                            }else{
                                SavingUtils.logOut();
                                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        } catch (JSONException e) {
                            //Error con el json
                            e.printStackTrace();
                        } catch (IOException e) {
                            //No hubo conexion, plox agrega algo
                            e.printStackTrace();
                        }
                        findViewById(R.id.progressBar).setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        OtherUtils.showInfoDialog(MainActivity.this,getString(R.string.alert_title_error_connection), getString(R.string.alert_error_connection));

                    }
                });
                return outfrag;
            /*case 2:
                outfrag= new EventFragment();
                return outfrag;
           */ case 1:
                outfrag = new PrizesFragment();
                loadHomeOnBackPress=true;
                findViewById(R.id.progressBar).setVisibility(View.GONE);

                return outfrag;
           /* case 4:
                outfrag = new ContactFragment();
                APIService apiServices= APIClient.getClient(this.getString(R.string.server_blood)).create(APIService.class);
                Call<ResponseBody> call2 = apiServices.me_reset(SavingUtils.preferences.getString(getString(R.string.shared_fbId),""),SavingUtils.preferences.getString(getString(R.string.shared_master1),""),SavingUtils.preferences.getString(getString(R.string.shared_master2),""));
                call2.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(MainActivity.this,"Good reset",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                return outfrag;
            */default:
                outfrag=new HomeFragment();

                return outfrag;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    public void loadHomeFragment(){
        //Selecting nav menu item
        selectNavMenu();

        //Setting the toolbar title
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        //Closing drawer on item click
        drawer.closeDrawers();

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            handler.post(mPendingRunnable);
        }

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!SavingUtils.isLoggedIn()){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: Ok, at least this shit let me in");

    }
}
