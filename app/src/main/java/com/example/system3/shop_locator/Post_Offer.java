package com.example.system3.shop_locator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Post_Offer extends AppCompatActivity {
    String title,description, bitimage="No_image";
    Integer locationid=404,userid=10001;
    EditText etitle,edescription;
    ImageView uploadimage,removepic,preview;
    Button postofferbtn;
    RelativeLayout relativeLayout;
    TextView change_image_text,error_text;
    public static Uri attachedImageUri=null;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAM_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_button);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setNavigationIcon(R.drawable.back_button_icon);

        etitle       = (EditText) findViewById(R.id._title);
        edescription = (EditText) findViewById(R.id._Offerdescription);
        postofferbtn   = (Button)findViewById(R.id._postOffer);
        uploadimage = (ImageView) findViewById(R.id.camera);
        relativeLayout =(RelativeLayout)findViewById(R.id.relativeimage1);
        change_image_text=(TextView)findViewById(R.id.cameratext);
        error_text=(TextView)findViewById(R.id.errortext);
        removepic=(ImageView) findViewById(R.id.remove_pic_img);
        preview=(ImageView) findViewById(R.id.relativeimage);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        removepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage.setVisibility(View.VISIBLE);
                change_image_text.setVisibility(View.VISIBLE);
                relativeLayout.setBackground(null);
                preview.setImageResource(0);
                removepic.setVisibility(View.GONE);
            }
        });


        postofferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = etitle.getText().toString();
                description = edescription.getText().toString();
                if (!validate()) {
                    onPostFailed();
                    return;
                }
                else{
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("Title", title);
                       postData.put("ImagePath", String.valueOf(attachedImageUri));
                        postData.put("Description",description);
                        postData.put("UserID", String.valueOf(userid));
                        postData.put("LocationID", String.valueOf(locationid));
                        new SendDeviceDetails().execute(Urls.url+Urls.postoffer, postData.toString());
                        Log.e("values",""+postData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Post_Offer.this);
                alertDialogBuilder.setMessage("Select Image");
                alertDialogBuilder.setPositiveButton("Open Camera",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Post_Offer.this,"Take a photo using camera",Toast.LENGTH_LONG).show();
                                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(i, RESULT_CAM_IMAGE);

                            }
                        });


                alertDialogBuilder.setNegativeButton("From gallery",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(Post_Offer.this,"upload from gallery",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                error_text.setVisibility(View.GONE);
            }
        });
    }




    public boolean validate() {
        boolean valid = true;

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
       // if (relativeLayout.getResources() == null)
       /* if (bitimage.equals("No_image")){
            error_text.setError("Please Choose An Image");
            error_text.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            change_image_text.setError(null);
        }*/


        return valid;
    }





    public void onPostFailed() {
        Toast.makeText(getBaseContext(), "Posting failed", Toast.LENGTH_LONG).show();

        postofferbtn.setEnabled(true);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        uploadimage.setVisibility(View.GONE);
        change_image_text.setVisibility(View.GONE);
        removepic.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case RESULT_LOAD_IMAGE:
                    if (resultCode == RESULT_OK) {

                   attachedImageUri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(attachedImageUri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filepath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bm = BitmapFactory.decodeFile(filepath);
                    //relativeLayout.setBackground(new BitmapDrawable(getBaseContext().getResources(), bm));

                        preview.setImageBitmap(bm);
                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    bitimage = Base64.encodeToString(b, Base64.DEFAULT);
                    }
                    else {
                        removepic.setVisibility(View.GONE);
                        uploadimage.setVisibility(View.VISIBLE);
                        change_image_text.setVisibility(View.VISIBLE);

                    }
                    break;
                case RESULT_CAM_IMAGE:
                    if (resultCode == RESULT_OK) {
                        attachedImageUri = data.getData();
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        //relativeLayout.setBackground(new BitmapDrawable(getBaseContext().getResources(), bm));
                        preview.setImageBitmap(bm);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        bitimage = Base64.encodeToString(b, Base64.DEFAULT);
                    }
                    else {
                        removepic.setVisibility(View.GONE);
                        uploadimage.setVisibility(View.VISIBLE);
                        change_image_text.setVisibility(View.VISIBLE);

                    }
                    break;
                default:
                    break;
            }

    }
    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        ProgressDialog p=new ProgressDialog(Post_Offer.this);
        @Override
        protected void onPreExecute() {
            p.setMessage("loading...");
            p.show();
            postofferbtn.setEnabled(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(Post_Offer.this, s, Toast.LENGTH_SHORT).show();
            Log.e("status", s);
            etitle.getText().clear();
            edescription.getText().clear();

            if (s.equals("true"))
            {
                p.dismiss();
                Toast.makeText(Post_Offer.this, "Successful", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else if (s.equals("false"))
            {
                p.dismiss();
                Toast.makeText(Post_Offer.this, "Not Posted", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else
            {
                Toast.makeText(Post_Offer.this, "Network Pblm...!!!", Toast.LENGTH_SHORT).show();
            }
            p.dismiss();
            Log.e("TAG", s); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

   /* private class postoffer extends AsyncTask<String, Void, String> {

        ProgressDialog p=new ProgressDialog(Post_Offer.this);

        @Override
        protected String doInBackground(String...  params) {
            ls=new ArrayList<Post_offer_bean_class>();

                p.dismiss();
            Post_offer_bean_class b=new Post_offer_bean_class();
            b.setTitle(title);
            b.setImage(String.valueOf(attachedImageUri));
            b.setDescription(description);
            b.setUserid(userid);
            b.setLocationid(locationid);


         *//*   ls.add(new BasicNameValuePair("ofr",));
            ls.add(new BasicNameValuePair("Title",title));
            ls.add(new BasicNameValuePair("Imagepath", String.valueOf(attachedImageUri)));
            ls.add(new BasicNameValuePair("Description",description));
            ls.add(new BasicNameValuePair("UserID",userid));
            ls.add(new BasicNameValuePair("LocationID",locationid));*//*

            String out=new Connect().Data(Urls.url+Urls.postoffer,ls);
            Log.e("ssss",out);
            return out;
        }

        @Override
        protected void onPreExecute()
        {
            p.setMessage("loading...");
            p.show();
            postofferbtn.setEnabled(false);

        }


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Post_Offer.this, s, Toast.LENGTH_SHORT).show();
            Log.i("status", s);
            etitle.getText().clear();
            edescription.getText().clear();

            if (s.equals("true"))
            {
                p.dismiss();
                Toast.makeText(Post_Offer.this, "Successful", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else if (s.equals("false"))
            {
                p.dismiss();
                Toast.makeText(Post_Offer.this, "Not Posted", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else
            {
                Toast.makeText(Post_Offer.this, "Network Pblm...!!!", Toast.LENGTH_SHORT).show();
            }
            p.dismiss();
        }
    }*/
}
