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

public class Post_Job extends AppCompatActivity {

    EditText etitle,edescription;
    String title,description,locationid="404",userid="10002";
    Button postjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__job);

        etitle       = (EditText) findViewById(R.id._title);
        edescription = (EditText) findViewById(R.id._jobdescription);
        postjob   = (Button)findViewById(R.id._postjob);

        postjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etitle.getText().toString();
                description = edescription.getText().toString();
                if (!validate()) {
                    onPostFailed();
                    return;
                }
                else{
                    new postjob().execute();
                }


            }
        });

    }
    public boolean validate() {
        boolean valid = true;


        title = etitle.getText().toString();
        description = edescription.getText().toString();

        if (title.isEmpty() || title.length() < 2) {
            etitle.setError("at least 2 characters");
            valid = false;
        } else {
            etitle.setError(null);
        }
        if (description.isEmpty() || description.length() < 2) {
            edescription.setError("at least 2 characters");
            valid = false;
        } else {
            edescription.setError(null);
        }


        return valid;
    }
    public void onPostFailed() {
        Toast.makeText(getBaseContext(), "Posting failed", Toast.LENGTH_LONG).show();

        postjob.setEnabled(true);
    }
    private class postjob extends AsyncTask<String, Void, String> {

        ProgressDialog p=new ProgressDialog(Post_Job.this);
        @Override
        protected void onPreExecute()
        {
            p.setMessage("loading...");
            p.show();
            postjob.setEnabled(false);

        }
        @Override
        protected String doInBackground(String... strings) {
            p.dismiss();
            ArrayList<NameValuePair> ls=new ArrayList<>();
            ls.add(new BasicNameValuePair("UserID",userid));
            ls.add(new BasicNameValuePair("Title",title));
            ls.add(new BasicNameValuePair("Description",description));
            ls.add(new BasicNameValuePair("LocationID",locationid));
            String out=new Connect().Data(Urls.url+Urls.postjob,ls);
            Log.e("ssss",out);
            return out;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Post_Job.this, s, Toast.LENGTH_SHORT).show();
            Log.i("status", s);
            etitle.getText().clear();
            edescription.getText().clear();

            if (s.equals("true"))
            {
                p.dismiss();
                Toast.makeText(Post_Job.this, "Successful", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else if (s.equals("false"))
            {
                p.dismiss();
                Toast.makeText(Post_Job.this, "Not Posted", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else
            {
                Toast.makeText(Post_Job.this, "Network Pblm...!!!", Toast.LENGTH_SHORT).show();
            }
            p.dismiss();
        }
    }
}
