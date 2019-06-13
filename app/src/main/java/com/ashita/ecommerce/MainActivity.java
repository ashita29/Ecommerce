package com.ashita.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ashita.ecommerce.adapter.CategoryRecyclerViewAdapter;
import com.ashita.ecommerce.adapter.CategoryViewHolder;
import com.ashita.ecommerce.adapter.ImageSliderAdapter;
import com.ashita.ecommerce.model.Category;
import com.ashita.ecommerce.utilities.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Utilities;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };

    private Timer timer;
    private int current_position =0;
    private ViewPager imageSlider;
    private LinearLayout dotsLayout;
    private int custom_position =0;
    FirebaseDatabase cFirebaseDatabase;
    DatabaseReference cRef;
    private RecyclerView categoryRecyclerView;
    private static View view;

    private static final String TAG = "MainActivity";
    private static final int NUM_COLUMNS =2;
    private ArrayList<String> cardViewImageUrls = new ArrayList<>();
    private ArrayList<String> cardViewNames = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        if(Common.checkInternetConnection(MainActivity.this))
        {
            callRecyclerView();
        }
        else
            Common.showInternetAlertMsg(MainActivity.this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        imageSlider = findViewById(R.id.image_slider);
        dotsLayout = findViewById(R.id.horizontal_dots);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
            }
        };

        if(Common.checkInternetConnection(MainActivity.this))
        {
            loadImageSlider();
            initCategoryRecyclerView();
        }
        else
        {
            Common.showInternetAlertMsg(MainActivity.this);
        }


        //Image Slider working
        //RecyclerView for categories
       // initCategoryData();


    }

    public void loadImageSlider()
    {
        ImageSliderAdapter imageAdapter = new ImageSliderAdapter(this,imageUrls);
        imageSlider.setAdapter(imageAdapter);
        prepareDots(custom_position++);
        createImageSlider();

        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(custom_position == imageUrls.length)
                {
                    custom_position = 0;
                }
                prepareDots(custom_position++);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public void callRecyclerView()
    {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(cRef,Category.class)
                .build();

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.card_view_category,viewGroup,false);
                        return new CategoryViewHolder(view);
                    }


                    @Override
                    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {

                        holder.setCategory(getApplicationContext(),model.getTitle(),model.getImageURL());
                    }
                };

        firebaseRecyclerAdapter.startListening();
        categoryRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
   /* public void initCategoryData()
    {
        Log.d(TAG, "initCategoryRecyclerView: is called");
        cardViewImageUrls.add("https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg");
        cardViewNames.add("cat");

        cardViewImageUrls.add("https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg");
        cardViewNames.add("glowworm");

        cardViewImageUrls.add("https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg");
        cardViewNames.add("road");

        cardViewImageUrls.add("https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg");
        cardViewNames.add("fantasy");

        cardViewImageUrls.add("https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg");
        cardViewNames.add("butterfly");

        initCategoryRecyclerView();
    }*/

    private void initCategoryRecyclerView() {

        Log.d(TAG, "initCategoryRecyclerView: called");
        categoryRecyclerView = findViewById(R.id.recycler_view_category);
        //CategoryRecyclerViewAdapter categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(this, cardViewNames,cardViewImageUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        //categoryRecyclerView.setAdapter(categoryRecyclerViewAdapter);

        //Firebase , send query to Firebase Database
        cFirebaseDatabase = FirebaseDatabase.getInstance();
        cRef = cFirebaseDatabase.getReference("MainPageCategoryList");
    }


    //For Auto Slider
    public void createImageSlider()
    {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(current_position == Integer.MAX_VALUE)
                {
                    current_position =0;
                }
                imageSlider.setCurrentItem(current_position++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(runnable);
            }
        },250,2500); // delay is for starting the event and 2500 is the delay between 2 slides.
    }

    //Horizontal dots
    private void prepareDots(int currentSlidePosition)
    {
        if(dotsLayout.getChildCount() > 0)
        {
            dotsLayout.removeAllViews();
        }
        ImageView dots[] = new ImageView[imageUrls.length];
        for(int i=0;i< imageUrls.length;i++)
        {
            dots[i]= new ImageView(this);
            if(i == currentSlidePosition)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dots));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,0,4,0);
            dotsLayout.addView(dots[i],layoutParams);

        }
    }


    /*End of SignIn Button*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        finishAffinity();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
