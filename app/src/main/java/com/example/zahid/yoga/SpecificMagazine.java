package com.example.zahid.yoga;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zahid.yoga.Adapter.MagazineImageAdapter;
import com.example.zahid.yoga.GetterSetter.Magazine;
import com.example.zahid.yoga.utill.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.Contacts.SettingsColumns.KEY;

public class SpecificMagazine extends android.support.v4.app.Fragment {
    private ImageView SpecificMagazineImage;
    private TextView SpecificMagazineName, SpecificMagazinePrice, SpecificMagazineDesc;


    View getView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.activity_specific_magazine, container,false);

        SpecificMagazineImage = (ImageView)getView.findViewById(R.id.specific_magazine_Image);
        SpecificMagazineName = (TextView)getView.findViewById(R.id.specific_magazine_Name);
        SpecificMagazinePrice = (TextView)getView.findViewById(R.id.specific_magazine_Price);
        SpecificMagazineDesc = (TextView)getView.findViewById(R.id.specific_magazine_Desc);


        int id = getArguments().getInt("STUFF");

        getSpecificMagazine(id);
        return getView;



    }



    void getSpecificMagazine (int id){
        String url = "http://yogamagazine.online/services/public/magzine/"+id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                        //JSONObject jsonObject = response.getJSONObject(null);
                        SpecificMagazineName.setText(response.getString("name"));
                        SpecificMagazineDesc.setText(response.getString("long_desc"));
                        SpecificMagazinePrice.setText("Price: £"+response.getString("price"));
                        JSONObject thumbnailObject = response.getJSONObject("thumbnail");
                        String imglink = thumbnailObject.getString("link");
                        Glide.with(getContext()).load(imglink).into(SpecificMagazineImage);
                        //magazine.id = jsonObject.getString("id");
                        //JSONObject thumbnailObject = jsonObject.getJSONObject("thumbnail");
                       // magazine.bitmap = thumbnailObject.getString("link");







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
