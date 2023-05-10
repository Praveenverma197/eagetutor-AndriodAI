package com.foresight.easychatgpt;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.foresight.utility.AvenuesParams;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity  extends AppCompatActivity {

    EditText etMobile;
    TextView tvRegister,txtMobile;
    Button btnlogin;
    //final String login_url = "https://www.eagetutor.com/jsp/Final/checkpayment.jsp?email=surojit19@gmail.com";
  //  final String login_url = "https://www.eagetutor.com/jsp/Final/checkpayment.jsp";
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMobile = (EditText) findViewById(R.id.et_Mobile);
        etMobile.setSingleLine();
        etMobile.setMaxLines(1);
        txtMobile = (TextView) findViewById(R.id.txt_Mobile);
        btnlogin = (Button) findViewById(R.id.btn_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
       // etPassword.setVisibility(View.GONE);
        String mobile = etMobile.getText().toString().trim();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final ImageView imgViewmobile = (ImageView)findViewById(R.id.iv_user);



        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent i = new Intent(LoginActivity.this, SignUp.class);
                    startActivity(i);

            }
        });

        //login activity

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//getting text from textbox
                String Mobile = etMobile.getText().toString();
               // String Password = etPassword.getText().toString();
                if(!(etMobile.getText().toString().isEmpty())) {
                    if (android.util.Patterns.PHONE.matcher(Mobile).matches() && Mobile.length()>9 && Mobile.length()<12)
                    {
                       // showToast("valid Mobile No");
                        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor prefsEditor = myPrefs.edit();
                        prefsEditor.putString("Unique_Mobile", Mobile);
                        prefsEditor.commit();
                        new LoginUser().execute(Mobile);
                        if(txtMobile.getText().toString().equalsIgnoreCase("User is Registered with us")){
                            new PaymentCheckOnMobile().execute(Mobile);

                        }
                        if(txtMobile.getText().toString().equalsIgnoreCase("Please registered with Eagetutor")){
                            showToast("Click On Register Button");

                        }
                        //Please registered with Eagetutor


                    }
                    else if(android.util.Patterns.PHONE.matcher(Mobile).matches() && Mobile.length()<9 ){
                        showToast("Please Enter  Mobile is less than 10 digit");
                        txtMobile.setText("Please Enter  Mobile is less than 10 digit");
                    }
                    else if( Mobile.length()>12 ){
                        showToast("Please Enter  Mobile is greater than 12 digit");
                        txtMobile.setText("Please Enter  Mobile is greater than 12 digit");
                    }
                    else
                    {
                        //showToast("Please Enter Valid Mobile");

                    }

                }else{
                    //showToast("Enter Mobile No is Empty");
                    txtMobile.setText("Enter MobileNo should not be blank");
                }


            }

            private SharedPreferences getSharedPreferences(String myPrefs, int modePrivate) {
                return null;
            }
        });

    }

    //requesting email and password from server

    public class LoginUser extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String Mobile= strings[0];
           // String Password= strings[1];
            String eage_url= AvenuesParams.Eage_Register_url+Mobile;

            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("email", Mobile)
                   // .add("user_password", Password)
                    .build();

            Request request = new Request.Builder()
                    .url(eage_url)
                    .get()
                    .build();


            //checking whether we are getting response from server or not

            Response response=null;
            try{
                response = okHttpClient.newCall(request).execute();


                if(response.isSuccessful()){
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String resultMessage = jsonObject.getString("message");

                    if(resultMessage.equalsIgnoreCase("User is Registered with Eagetutor")){
                       // showToast("User is Registered with us");
                        txtMobile.setText("User is Registered with us");
                        btnlogin.setText("Continue");
                        Intent i= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                   else if(resultMessage.equalsIgnoreCase("User is not Registered with Eagetutor")){

                       // showToast("you are not register with us");
                        txtMobile.setText("Kindly do registered with Eagetutor");

                   }

                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;



        }


    }
    public class PaymentCheckOnMobile extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String Mobile= strings[0];
            // String Password= strings[1];
            String eage_url_check_payment= AvenuesParams.Eage_Payment_Check_Mobile_URL+Mobile;

            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("mobile", Mobile)
                    // .add("user_password", Password)
                    .build();

            Request request = new Request.Builder()
                    .url(eage_url_check_payment)
                    .get()
                    .build();


            //checking whether we are getting response from server or not

            Response response=null;
            try{
                response = okHttpClient.newCall(request).execute();


                if(response.isSuccessful()){
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String resultMessage = jsonObject.getString("message");

                    if(resultMessage.equalsIgnoreCase("Your payment is done")){
                       // showToast("Your payment is done");
                        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor prefsEditor = myPrefs.edit();
                        prefsEditor.putString("Unique_PaymentFlag", "Payment Done");
                        prefsEditor.commit();
                        Thread.sleep(5000);
                        Intent i= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{

                       // showToast("Please buy the Eage AL Chat Service");
                        txtMobile.setText("Please use the Eage AL Chat Service");
                        //for time being payment screen is not required
                      //  Intent i= new Intent(LoginActivity.this, InitialScreenActivity.class);
                        Intent i= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;



        }


    }


    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

}
