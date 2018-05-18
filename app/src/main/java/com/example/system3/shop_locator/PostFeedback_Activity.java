package com.example.system3.shop_locator;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class PostFeedback_Activity extends AppCompatActivity {
    EditText efeedback;
    Button post;
    String feedback,userId="10001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feedback);
        efeedback=(EditText)findViewById(R.id._feedback);
        post=(Button)findViewById(R.id._postfeedback);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback = efeedback.getText().toString();
                if (!validate()) {
                    onPostFailed();
                    return;
                }
                else{
                    new postfeedback().execute();
                }

            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        feedback = efeedback.getText().toString();


        if (feedback.isEmpty() || feedback.length() < 2) {
            efeedback.setError("at least 2 characters");
            valid = false;
        } else {
            efeedback.setError(null);
        }


        return valid;
    }

    public void onPostFailed() {
        Toast.makeText(getBaseContext(), "Posting failed", Toast.LENGTH_LONG).show();

        post.setEnabled(true);
    }
    private class postfeedback extends AsyncTask<String, Void, String>{

        ProgressDialog p=new ProgressDialog(PostFeedback_Activity.this);


        @Override
        protected void onPreExecute()
        {
            p.setMessage("loading...");
            p.show();
            post.setEnabled(false);

        }
        @Override
        protected String doInBackground(String... strings) {
            p.dismiss();
            ArrayList<NameValuePair> ls=new ArrayList<>();
            ls.add(new BasicNameValuePair("Feedback",feedback));
            ls.add(new BasicNameValuePair("PostedBy",userId));
            String out=new Connect().Data(Urls.url+Urls.postfeedback,ls);
            Log.e("ssss",out);
            return out;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(PostFeedback_Activity.this, s, Toast.LENGTH_SHORT).show();
            Log.i("status", s);
            efeedback.getText().clear();

            if (s.equals("true"))
            {
                p.dismiss();
                Toast.makeText(PostFeedback_Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else if (s.equals("false"))
            {
                p.dismiss();
                Toast.makeText(PostFeedback_Activity.this, "Not Posted", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else
            {
                Toast.makeText(PostFeedback_Activity.this, "Network Pblm...!!!", Toast.LENGTH_SHORT).show();
            }
            p.dismiss();
        }
    }
    }

