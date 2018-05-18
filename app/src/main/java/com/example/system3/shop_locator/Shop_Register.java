package com.example.system3.shop_locator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.di;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class Shop_Register extends AppCompatActivity {
    TextView login;
RadioGroup eLocation;
    GPSTracker gps;
    Spinner etype;
    EditText etitle,ecity,edescription,ecategory,ephone,ewebsite,ekeywords,eaddress,evisible_area,eemail,edistrict,estate;
    String title,city,description,category,phone,website,keywords,address,visible_area,latitude,longitude,email,district,state,type;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register);
        gps=new GPSTracker(Shop_Register.this);
        eLocation=(RadioGroup)findViewById(R.id.location);
        etitle = (EditText)findViewById(R.id.title);
        ecity = (EditText)findViewById(R.id.city);
        edescription = (EditText)findViewById(R.id.description);
        ecategory = (EditText)findViewById(R.id.category);
        ephone = (EditText)findViewById(R.id.phone);
        ewebsite = (EditText)findViewById(R.id.website);
        ekeywords = (EditText)findViewById(R.id.keywords);
        etype=(Spinner)findViewById(R.id.type_spinner);
        edistrict = (EditText)findViewById(R.id.District);
        estate = (EditText)findViewById(R.id.State);
        eaddress = (EditText)findViewById(R.id.address);
        evisible_area = (EditText)findViewById(R.id.visible_area);
        eemail = (EditText)findViewById(R.id.email);
        login = (TextView) findViewById(R.id.link_login);
        signup = (Button) findViewById(R.id.btn_signup);
        eLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
              final  RadioButton rb=(RadioButton)findViewById(checkedId);
                Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String value =rb.getText().toString();
                        if(value.equals("Use Current"))
                        {
                            latitude= String.valueOf(gps.getLatitude());
                            longitude= String.valueOf(gps.getLongitude());
                            Log.e("llllcurrent",latitude+longitude);
                        }
                        if(value.equals("From map"))
                        {
                            startActivity(new Intent(Shop_Register.this,MapsActivity.class));
                            latitude=MapsActivity.lat;
                            longitude=MapsActivity.log;
                            Log.e("llllmap",latitude+longitude);
                        }
                    }
                });
                // checkedId is the RadioButton selected
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        title=etitle.getText().toString();
                        city=ecity.getText().toString();
                        description=edescription.getText().toString();
                        category=ecategory.getText().toString();
                        phone=ephone.getText().toString();
                        website=ewebsite.getText().toString();
                        keywords=ekeywords.getText().toString();
                        address=eaddress.getText().toString();
                        visible_area=evisible_area.getText().toString();
                        email=eemail.getText().toString();
                        district=edistrict.getText().toString();
                        state=estate.getText().toString();
                        type=etype.getSelectedItem().toString();

                if (!validate()) {
                    onSignupFailed();
                    return;
                }
                else{
                    new asy().execute();
                }
            }
        });

    }

    private class asy extends AsyncTask <String, Void, String>{
        ProgressDialog p=new ProgressDialog(Shop_Register.this);

        @Override
        protected void onPreExecute() {
            p.setMessage("loading...");
            p.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> ShopRegisterlist=new ArrayList<>();
            ShopRegisterlist.add(new BasicNameValuePair("Title",title));
            ShopRegisterlist.add(new BasicNameValuePair("City",city));
            ShopRegisterlist.add(new BasicNameValuePair("Category",category));
            //ShopRegisterlist.add(new BasicNameValuePair("Description",description));
            ShopRegisterlist.add(new BasicNameValuePair("Phone",phone));
            //ShopRegisterlist.add(new BasicNameValuePair("Website",website));
            //ShopRegisterlist.add(new BasicNameValuePair("Keywords",keywords));
            //ShopRegisterlist.add(new BasicNameValuePair("Visible Area",visible_area));
            ShopRegisterlist.add(new BasicNameValuePair("Latitude",latitude));
            ShopRegisterlist.add(new BasicNameValuePair("Longitude",longitude));
            ShopRegisterlist.add(new BasicNameValuePair("Email",email));
            ShopRegisterlist.add(new BasicNameValuePair("Address",address));
            ShopRegisterlist.add(new BasicNameValuePair("District",district));
            ShopRegisterlist.add(new BasicNameValuePair("State",state));
            ShopRegisterlist.add(new BasicNameValuePair("Type",type));

            Log.e("ttt", String.valueOf(ShopRegisterlist));
            String out=new Connect().Data(Urls.url+Urls.shopsignup,ShopRegisterlist);
            Log.e("ssss",out);
            return out;

        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(Shop_Register.this, ""+s, Toast.LENGTH_SHORT).show();
            Log.i("status", s);

            if (s.equals("true"))
            {
                p.dismiss();
                Toast.makeText(Shop_Register.this, "values send successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Shop_Register.this, Home.class));
            }
            else if(s.equals("false"))
            {
                Toast.makeText(Shop_Register.this, "Try again..... Invalid data", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Shop_Register.this, "Network Problem Detected...!!!", Toast.LENGTH_SHORT).show();
            }
            p.dismiss();

        }
    }
    public boolean validate() {
        boolean valid = true;
        title=etitle.getText().toString();
        city=ecity.getText().toString();
        description=edescription.getText().toString();
        category=ecategory.getText().toString();
        phone=ephone.getText().toString();
        website=ewebsite.getText().toString();
        keywords=ekeywords.getText().toString();
        address=eaddress.getText().toString();
        visible_area=evisible_area.getText().toString();
        email=eemail.getText().toString();
        district=edistrict.getText().toString();
        state=estate.getText().toString();

        if (title.isEmpty() || title.length() < 3) {
            etitle.setError("at least 3 characters");
            valid = false;
        } else {
            etitle.setError(null);
        }
        if (city.isEmpty() || city.length() < 3) {
            ecity.setError("at least 3 characters");
            valid = false;
        } else {
            ecity.setError(null);
        }
        if (category.isEmpty() || category.length() < 3) {
            ecategory.setError("at least 3 characters");
            valid = false;
        } else {
            ecategory.setError(null);
        }
        if (phone.isEmpty() || phone.length()<10) {
            ephone.setError("enter a valid phone number");
            valid = false;
        } else {
            ephone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eemail.setError("enter a valid email address");
            valid = false;
        } else {
            eemail.setError(null);
        }
        if (address.isEmpty() || address.length() < 5) {
            eaddress.setError("at least 5 characters");
            valid = false;
        } else {
            eaddress.setError(null);
        }
        if (district.isEmpty() || district.length() < 3) {
            edistrict.setError("at least 3 characters");
            valid = false;
        } else {
            edistrict.setError(null);
        }
        if (state.isEmpty() || state.length() < 3) {
            estate.setError("at least 3 characters");
            valid = false;
        } else {
            estate.setError(null);
        }


        return valid;
    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        signup.setEnabled(true);
    }
}
