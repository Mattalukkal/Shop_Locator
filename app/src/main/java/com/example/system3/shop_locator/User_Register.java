package com.example.system3.shop_locator;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class User_Register extends AppCompatActivity {
    private static final String TAG = "User_Register";
    EditText _nameText,_emailText,_phone;
    String name,email,phone;
    Button _signupButton;
    TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__register);


        _nameText       = (EditText) findViewById(R.id.input_name);
        _emailText      = (EditText) findViewById(R.id.input_email);
        _phone          = (EditText) findViewById(R.id.input_phone);
        _signupButton   = (Button)findViewById(R.id.btn_signup);
        _loginLink      = (TextView)findViewById(R.id.link_login);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 name = _nameText.getText().toString();
                 email = _emailText.getText().toString();
                 phone = _phone.getText().toString();
                if (!validate()) {
                onSignupFailed();
                return;
                   }
                else{
                    new signup().execute();
                }


            }
        });

    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
         email = _emailText.getText().toString();
        phone = _phone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (phone.isEmpty() || phone.length()<10) {
            _phone.setError("enter a valid phone number");
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

    private class signup extends AsyncTask <String, Void, String>{
        ProgressDialog p=new ProgressDialog(User_Register.this);
        @Override
        protected void onPreExecute()
        {
            p.setMessage("loading...");
            p.show();
            _signupButton.setEnabled(false);

        }

        @Override
        protected String doInBackground(String... strings) {

            p.dismiss();
            ArrayList<NameValuePair> ls=new ArrayList<>();
            ls.add(new BasicNameValuePair("Name",name));
            ls.add(new BasicNameValuePair("Phone",phone));
            ls.add(new BasicNameValuePair("Email",email));
            Log.e("ttt", String.valueOf(ls));
            String out=new Connect().Data(Urls.url+Urls.usersignup,ls);
            Log.e("ssss",out);
            return out;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(User_Register.this, s, Toast.LENGTH_SHORT).show();
            Log.i("status", s);
            Toast.makeText(User_Register.this, "Successful", Toast.LENGTH_SHORT).show();
            /*if (s.equals("success"))
            {
                p.dismiss();

                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }*/
           /* else
            {
                Toast.makeText(User_Register.this, "Network Pblm...!!!", Toast.LENGTH_SHORT).show();
            }*/
            p.dismiss();
        }
        }

}
