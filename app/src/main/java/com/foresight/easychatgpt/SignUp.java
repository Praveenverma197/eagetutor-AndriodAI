package com.foresight.easychatgpt;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {


    EditText etName, etEmail, etMobile;

    TextView txtname,textViewemail,textViewmobile;
    Button btnRegister,btnStartChat;
    final String url_Register= "https://www.eagetutor.com/jsp/Final/ielts_user.jsp";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email,phone;
//https://www.eagetutor.com/jsp/Final/ielts_user.jsp?uname=faf&email=user8889@mail.com&mobile=9871515504
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName= (EditText) findViewById(R.id.et_name);
        etEmail= (EditText) findViewById(R.id.et_reg_email);
        etMobile= (EditText) findViewById(R.id.et_reg_password);
        btnRegister= (Button) findViewById(R.id.btn_register);
        btnStartChat= (Button) findViewById(R.id.btn_Start_Chat);
        btnStartChat.setVisibility(View.GONE);
        etMobile.setSingleLine();
        etMobile.setMaxLines(1);
        etName.setSingleLine();
        etName.setMaxLines(1);
        etEmail.setSingleLine();
        etEmail.setMaxLines(1);
          txtname = (TextView)findViewById(R.id.tv_name);
        textViewemail = (TextView)findViewById(R.id.tv_reg_email);
        textViewmobile = (TextView)findViewById(R.id.tv_reg_password);
         email = etEmail.getText().toString().trim();
         phone = etMobile.getText().toString().trim();
       // String pattern = "^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$";
        //Matcher m;

        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("MyPref", 0);
        String sharedMobile = myPrefs.getString("Unique_Mobile", null);
        etMobile.setText(sharedMobile);
        etMobile.setEnabled(true);


        etEmail .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (email.matches(emailPattern) && s.length() > 0)
                {
                   // Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    // or
                   // textViewemail.setText("valid email");
                }
                else
                {
                   // Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    //or
                  //  textViewemail.setText("Please Enter Valid email");
                   // textViewemail.setHighlightColor(Color.RED);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(android.util.Patterns.PHONE.matcher(phone).matches())
                // using android available method of checking phone
                {
                    //Toast.makeText(getApplicationContext(),"valid Mobile No",Toast.LENGTH_SHORT).show();
                    // or
                   // textViewmobile.setText("valid Mobile");
                }
                else
                {
                   // Toast.makeText(getApplicationContext(),"Please Enter valid Mobile No",Toast.LENGTH_SHORT).show();
                    // or
                   // textViewmobile.setText("Invalid Mobile");
                   // textViewmobile.setHighlightColor(Color.RED);
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name= etName.getText().toString();
                String Email= etEmail.getText().toString();
                String Mobile=etMobile.getText().toString();
                String btntext=btnRegister.getText().toString();
                if(btntext.equalsIgnoreCase("Start Chat")){
                    Intent i= new Intent(SignUp.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

              if(Name.isEmpty()){
                  txtname.setText("Name should not be blank");
              }
              else{
                  txtname.setText("Name");
              }
              if(Email.isEmpty()){
                   textViewemail.setText("Email Should not be blank");
              }
              if(Mobile.isEmpty()){
                  textViewmobile.setText("Mobile Should not be blank");
                }
              if(!(Email.isEmpty())){
                  if (Email.matches(emailPattern))
                  {
                      textViewemail.setText("valid email");
                  }
                  else
                  {
                      textViewemail.setText("Please Enter Valid email");
                      //return false;

                  }
              }
              if(!(Mobile.isEmpty())){
                  if(android.util.Patterns.PHONE.matcher(Mobile).matches() && Mobile.length()>9 && Mobile.length()<13)
                  // using android available method of checking phone
                  {

                      textViewmobile.setText("valid Mobile");
                  }
                  else
                  {
                      textViewmobile.setText("Invalid Mobile");

                  }
              }
              if(textViewemail.getText().toString().equalsIgnoreCase("valid email") && textViewmobile.getText().toString().equalsIgnoreCase("valid Mobile") ) {
                    new RegisterUser().execute(Name, Email, Mobile);
                }

            }
        });
    }

    public class RegisterUser extends AsyncTask<String,Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String Name= strings[0];
            String Email= strings[1];
            String Password=strings[2];
            String finalurl=url_Register+"?uname="+Name+
                    "&email="+Email+
                    "&mobile="+Password+
                    "&platform=Andriod_EageTutorAl";

            OkHttpClient okHttpClient=new OkHttpClient();
            Request request = new Request.Builder()
                    .url(finalurl)
                    .get()
                    .build();


            //checking server response and inserting data

            Response response= null;

            try {
                response= okHttpClient.newCall(request).execute();

                //JSONObject jsonObject = new JSONObject(response.body().string());
                if(response.isSuccessful()){

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String resultMessage = jsonObject.getString("message");
                    if(resultMessage.equalsIgnoreCase("New Registration is done with Eagetutor")){
                        btnRegister.setText("Start Chat");

                       // showToast("New Registration is done with Eagetutor");
                       // etEmail.setText("");
                        //etMobile.setText("");
                       // etName.setText("");
                        //etMobile.setEnabled(true);

                       // btnStartChat.setVisibility(View.VISIBLE);
                    }
                    else if (resultMessage.equalsIgnoreCase("User is Registered with Eagetutor")){
                        //Thread.sleep(5000);

                        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("MyPref", 0);
                        String sharedpaymentFlag = myPrefs.getString("Unique_PaymentFlag", null);
                        if(sharedpaymentFlag.equalsIgnoreCase("Payment Done")){
                            Intent i = new Intent(SignUp.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        }else {
                            showToast("User is Registered with Eagetutor.Kindly do Payment");
                            Intent i = new Intent(SignUp.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        // btnStartChat.setVisibility(View.VISIBLE);
                    }
                    else {

                    }
                   // String result= response.body().string();
                   // showToast(result);

                }

            }
            catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignUp.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
