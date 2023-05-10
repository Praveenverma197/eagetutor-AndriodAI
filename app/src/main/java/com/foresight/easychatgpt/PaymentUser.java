package com.foresight.easychatgpt;

import android.content.Intent;
import android.os.AsyncTask;

import com.foresight.utility.AvenuesParams;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentUser extends AsyncTask<String, Void, String> {
     @Override
    protected String doInBackground(String... strings) {
        String Mobile= strings[0];
        String Result="";

        OkHttpClient okHttpClient= new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("mobile", Mobile)
                .build();
        String Payurl=AvenuesParams.Eage_Payment_Register_Mobile_url+Mobile;

        Request request = new Request.Builder()
                //.addHeader(AvenuesParams.X_Api_Key,AvenuesParams.X_Api_Value)
                .url(Payurl)
                .post(formBody)
                .build();


        //checking whether we are getting response from server or not

        Response response=null;
        try{
            response = okHttpClient.newCall(request).execute();


            if(response.isSuccessful()){
                JSONObject jsonObject = new JSONObject(response.body().string());
                String resultMessage = jsonObject.getString("message");

                if(resultMessage.equalsIgnoreCase("New Payment is register over this email")){
                    Result="New Payment is register over this email";

                }else if (resultMessage.equalsIgnoreCase("Kindly do register this email")){
                    Result=" Kindly do register this email";
                }else{
                    Result=" Payment is already done";
                }


            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return Result;



    }



}
