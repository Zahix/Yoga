package com.example.zahid.yoga;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zahid.yoga.Adapter.MagazineImageAdapter;
import com.example.zahid.yoga.Fragments.AllMagazine;
import com.example.zahid.yoga.Fragments.SpecificMagazine;
import com.example.zahid.yoga.GetterSetter.Magazine;
import com.example.zahid.yoga.utill.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    String name,BannerImglink, BannerWeblink ;
    private RecyclerView recyclerView;
    private MagazineImageAdapter magazineImageAdapter;
    private List<Magazine> magazines ;
    private Magazine magazine;

    private ImageView Ad_Banner_Img, Main_Magazine_Img;

    private static int CURRENT_FRAGMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Ad_Banner_Img = (ImageView)findViewById(R.id.ad_Banners);
        Ad_Banner_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(BannerWeblink));
                startActivity(intent);
            }
        });

        Main_Magazine_Img = (ImageView)findViewById(R.id.main_magazine) ;

        getMagzine();
        getAdBanner();











    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(CURRENT_FRAGMENT == 1){
                CURRENT_FRAGMENT = 0;
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);

        }

        if(CURRENT_FRAGMENT == 0){
                super.onBackPressed();
        }
        else {
            super.onBackPressed();
        }



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

        if (id == R.id.nav_Issue) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            CURRENT_FRAGMENT = 1;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new AllMagazine();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();


        } else if (id == R.id.nav_Magazine) {
            CURRENT_FRAGMENT = 1;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new AllMagazine();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();

        } else if (id == R.id.nav_about) {


        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_privacy) {

        } else if (id == R.id.nav_SignIn){
            Toast.makeText(getApplicationContext(),"signIn",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  void getMagzine(){
        String url = "http://yogamagazine.online/services/public/magzine?api_token=CP26zgTXIfNltPbDYfl48uta7s4eHszdtYWBGjnywFlPuacgbDGQPEEQSEQp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                magazines = new ArrayList<Magazine>();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        final Magazine magazine = new Magazine();
                        JSONObject jsonObject = response.getJSONObject(i);
                        magazine.name = jsonObject.getString("name");
                        magazine.id = jsonObject.getString("id");
                        JSONObject thumbnailObject = jsonObject.getJSONObject("thumbnail");
                        magazine.bitmap = thumbnailObject.getString("link");


                        magazines.add(magazine);
                        if (Common.firstThumbnail){
                            //get your value
                            String imglink = String.valueOf(magazine.bitmap);
                            Glide.with(context).load(imglink).into(Main_Magazine_Img);
                            Common.firstThumbnail = false;
                            Main_Magazine_Img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int postId = Integer.parseInt(magazine.id);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("STUFF", postId);
                                    CURRENT_FRAGMENT = 1;
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    Fragment fragment = new SpecificMagazine();
                                    fragment.setArguments(bundle);
                                    transaction.replace(R.id.content_frame, fragment);
                                    transaction.commit();
                                }
                            });
                        }


                    }
                    Common.firstThumbnail = true;
                    // initalize magazines********

                    // magazine = new Magazine();

                    // initalize RecyclerView********
                    recyclerView = (RecyclerView)findViewById(R.id.recycle_list);
                    magazineImageAdapter = new MagazineImageAdapter(MainActivity.this, magazines, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {

                            int postId = Integer.parseInt(magazines.get(position).id);
                            Bundle bundle = new Bundle();
                            bundle.putInt("STUFF", postId);
                            CURRENT_FRAGMENT = 1;
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            Fragment fragment = new SpecificMagazine();
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.content_frame, fragment);
                            transaction.commit();
// set Fragmentclass Arguments
//                            Fragmentclass fragobj = new Fragmentclass();
//                            fragobj.setArguments(bundle);

                        }
                    });
                    recyclerView.setAdapter(magazineImageAdapter);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2, GridLayoutManager.HORIZONTAL,false);
//                    recyclerView.setLayoutManager(gridLayoutManager);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });
    }

    public  void  getAdBanner(){
        String url = "http://yogamagazine.online/services/public/banners?api_token=CP26zgTXIfNltPbDYfl48uta7s4eHszdtYWBGjnywFlPuacgbDGQPEEQSEQp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {

                        JSONObject jsonObject = response.getJSONObject(1);
                        BannerImglink = jsonObject.getString("banner_img");
                        BannerWeblink = jsonObject.getString("banner_link");
                    Glide.with(context).load(BannerImglink).into(Ad_Banner_Img);





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }

        });

    }

//    public  void getMagzine(){
//        String url = "http://yogamagazine.online/services/public/magzine?api_token=CP26zgTXIfNltPbDYfl48uta7s4eHszdtYWBGjnywFlPuacgbDGQPEEQSEQp";
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(url, new JsonHttpResponseHandler(){
//
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
//
//                magazines = new ArrayList<Magazine>();
//
//                try {
//                    for (int i = 0; i < response.length(); i++) {
//
//                        Magazine magazine = new Magazine();
//
//                        JSONObject jsonObject = response.getJSONObject(i);
//                        JSONObject thumbnailObject = jsonObject.getJSONObject("thumbnail");
//                        magazine.name = thumbnailObject.getString("name");
//
//                        magazines.add(magazine);
//                    }
//
//                    // initalize RecyclerView********
//                    recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
//                    magazineImageAdapter = new MagazineImageAdapter(MainActivity.this,magazines);
//                    recyclerView.setAdapter(magazineImageAdapter);
//                    recyclerView.setHasFixedSize(true);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
//                    recyclerView.setLayoutManager(mLayoutManager);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
//                    recyclerView.setLayoutManager(gridLayoutManager);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onRetry(int retryNo) {
//                super.onRetry(retryNo);
//            }
//        });
//    }
}
