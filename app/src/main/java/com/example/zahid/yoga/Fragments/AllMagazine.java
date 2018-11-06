package com.example.zahid.yoga.Fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zahid.yoga.Adapter.All_MagazineAdapter;
import com.example.zahid.yoga.Adapter.MagazineImageAdapter;
import com.example.zahid.yoga.CustomItemClickListener;
import com.example.zahid.yoga.GetterSetter.Magazine;
import com.example.zahid.yoga.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllMagazine extends android.support.v4.app.Fragment {

    Context context;
    String name,link ;
    private RecyclerView recyclerView;
    private All_MagazineAdapter all_magazineAdapter;
    private List<Magazine> magazines;
    Magazine magazine;

    private static int CURRENT_FRAGMENT = 0;

    View getView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.activity_all_magazine, container,false);

        getMagzine();
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
                        Magazine magazine = new Magazine();
                        JSONObject jsonObject = response.getJSONObject(i);
                        magazine.name = jsonObject.getString("name");
                        magazine.id = jsonObject.getString("id");
                        JSONObject thumbnailObject = jsonObject.getJSONObject("thumbnail");
                        magazine.bitmap = thumbnailObject.getString("link");

                        //  Bitmap myBitmap = BitmapFactory.decodeStream((InputStream)url.openConnection().getInputStream());
                        //    magazine.setImageView(myBitmap);
                       // magazine = new Magazine();
                        //magazine.setName(name);
                        magazines.add(magazine);


                    }

                    // initalize magazines********

                    // magazine = new Magazine();



                    // initalize RecyclerView********
                    recyclerView = (RecyclerView) getView.findViewById(R.id.recycle_view);
                    all_magazineAdapter = new All_MagazineAdapter(getContext(), magazines, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            int postId = Integer.parseInt(magazines.get(position).id);
                            Bundle bundle = new Bundle();
                            bundle.putInt("STUFF", postId);
                            CURRENT_FRAGMENT = 1;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment fragment = new SpecificMagazine();
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.content_frame, fragment);
                            transaction.commit();
                        }
                    });
                    recyclerView.setAdapter(all_magazineAdapter);
                    recyclerView.setHasFixedSize(true);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);





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
