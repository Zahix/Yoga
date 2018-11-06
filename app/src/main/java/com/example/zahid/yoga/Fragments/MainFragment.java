package com.example.zahid.yoga.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zahid.yoga.Adapter.MagazineImageAdapter;
import com.example.zahid.yoga.CustomItemClickListener;
import com.example.zahid.yoga.GetterSetter.Magazine;
import com.example.zahid.yoga.MainActivity;
import com.example.zahid.yoga.R;
import com.example.zahid.yoga.utill.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zahid on 06-Nov-18.
 */

public class MainFragment extends android.app.Fragment{

    Context context;
    String name,BannerImglink, BannerWeblink ;
    private RecyclerView recyclerView;
    private MagazineImageAdapter magazineImageAdapter;
    private List<Magazine> magazines ;
    private ImageView Ad_Banner_Img, Main_Magazine_Img;

    private static int CURRENT_FRAGMENT = 0;

    View getView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.fragment_main, container,false);

        context = getView.getContext();

        Ad_Banner_Img = (ImageView)getView.findViewById(R.id.ad_Banners);
        Ad_Banner_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(BannerWeblink));
                startActivity(intent);
            }
        });

        Main_Magazine_Img = (ImageView)getView.findViewById(R.id.main_magazine) ;

        getMagzine();
        getAdBanner();

        return getView;

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
                                    FragmentManager manager = getFragmentManager();
                                    manager.popBackStack();
                                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                                    SpecificMagazine specificMagazine = new SpecificMagazine();
                                    transaction.remove(specificMagazine);
                                    specificMagazine.setArguments(bundle);
                                    transaction.add(R.id.content_frame, specificMagazine,"SpecificMagazine");
                                    transaction.addToBackStack(specificMagazine.getClass().getName()).commit();


                                }
                            });
                        }


                    }
                    Common.firstThumbnail = true;
                    // initalize magazines********

                    // magazine = new Magazine();

                    // initalize RecyclerView********
                    recyclerView = (RecyclerView)getView.findViewById(R.id.recycle_list);
                    magazineImageAdapter = new MagazineImageAdapter(context, magazines, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {

                            int postId = Integer.parseInt(magazines.get(position).id);
                            Bundle bundle = new Bundle();
                            bundle.putInt("STUFF", postId);
                            CURRENT_FRAGMENT = 1;
                            FragmentManager manager = getFragmentManager();
                            manager.popBackStack();
                            android.app.FragmentTransaction transaction = manager.beginTransaction();
                            SpecificMagazine  specificMagazine = new SpecificMagazine();
                            specificMagazine.setArguments(bundle);
                            transaction.add(R.id.content_frame, specificMagazine);
                            transaction.addToBackStack(specificMagazine.getClass().getName()).commit();
// set Fragmentclass Arguments
//                            Fragmentclass fragobj = new Fragmentclass();
//                            fragobj.setArguments(bundle);

                        }
                    });
                    recyclerView.setAdapter(magazineImageAdapter);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
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
}
