package com.example.zahid.yoga.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zahid.yoga.R;
import com.example.zahid.yoga.utill.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpecificMagazine extends android.app.Fragment {
    private ImageView SpecificMagazineImage;
    private TextView SpecificMagazineName, SpecificMagazinePrice, SpecificMagazineDesc;
    private Button Subscribe;

    Context context;
    View getView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.activity_specific_magazine, container,false);

        context = getView.getContext();

        SpecificMagazineImage = (ImageView)getView.findViewById(R.id.specific_magazine_Image);
        SpecificMagazineName = (TextView)getView.findViewById(R.id.specific_magazine_Name);
        SpecificMagazinePrice = (TextView)getView.findViewById(R.id.specific_magazine_Price);
        SpecificMagazineDesc = (TextView)getView.findViewById(R.id.specific_magazine_Desc);

        Subscribe = (Button)getView.findViewById(R.id.btn_subscribe);


        int id = getArguments().getInt("STUFF");

        getSpecificMagazine(id);

        Subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.loginStatus.equals("false")){
                    FragmentManager manager = getFragmentManager();
                    manager.popBackStack();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    LoginActivity loginActivity = new LoginActivity();
                    transaction.add(R.id.content_frame, loginActivity);
                    transaction.addToBackStack(loginActivity.getClass().getName()).commit();

                }
                else {
                    FragmentManager manager = getFragmentManager();
                    manager.popBackStack();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    Subscription_Payment subscription_payment = new Subscription_Payment();
                    transaction.add(R.id.content_frame, subscription_payment);
                    transaction.addToBackStack(subscription_payment.getClass().getName()).commit();

                }



            }
        });



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
                        Glide.with(context).load(imglink).into(SpecificMagazineImage);
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
