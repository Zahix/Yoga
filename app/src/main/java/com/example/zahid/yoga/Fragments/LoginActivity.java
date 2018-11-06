package com.example.zahid.yoga.Fragments;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zahid.yoga.R;
import com.example.zahid.yoga.utill.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends android.app.Fragment {

    private EditText email,password;
    private Button Login;

    View getView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.activity_login, container,false);

        email = (EditText)getView.findViewById(R.id.editTxt_Email);
        password = (EditText)getView.findViewById(R.id.editTxt_Pass);

        Login = (Button)getView.findViewById(R.id.btn_Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        return getView;
    }


    void Login(){

        String url = "http://yogamagazine.online/services/public/user/login";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email.getText());
        params.put("password",password.getText());
        client.post(url,params, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String loginStatus;
                try {
                    if(response.getString("success").equals("true")){
                        loginStatus = "Login is SuccessFull";
                        Common.loginStatus = loginStatus;

                    }
                    else {
                        loginStatus = "Login is Failed";
                    }

                    Toast.makeText(getContext(),loginStatus,Toast.LENGTH_LONG).show();
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


