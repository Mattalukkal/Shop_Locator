package com.example.system3.shop_locator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post_ads extends AppCompatActivity {
    String title,description,latitude,longitude,bitimage="No_image";
    Integer userid=10001;
    GPSTracker gps;
    EditText etitle,edescription;
    ImageView uploadimage,removepic,preview;
    Button postadsbtn;
    RelativeLayout relativeLayout;
    TextView change_image_text,error_text;
    public static Uri attachedImageUri=null;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAM_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad_);

        gps=new GPSTracker(Post_ads.this);
        etitle       = (EditText) findViewById(R.id._title);
        edescription = (EditText) findViewById(R.id._description);
        postadsbtn   = (Button)findViewById(R.id._postads);
        uploadimage = (ImageView) findViewById(R.id.camera);
        relativeLayout =(RelativeLayout)findViewById(R.id.relativeimage1);
        change_image_text=(TextView)findViewById(R.id.cameratext);
        error_text=(TextView)findViewById(R.id.errortext);
        removepic=(ImageView) findViewById(R.id.remove_pic_img);
        preview=(ImageView) findViewById(R.id.relativeimage);


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


        postadsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = etitle.getText().toString();
                description = edescription.getText().toString();
                latitude= String.valueOf(gps.getLatitude());
                longitude= String.valueOf(gps.getLongitude());
                if (!validate()) {
                    onPostFailed();
                    return;
                }
                else{
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("Title", title);
                        postData.put("ImagePath", String.valueOf(attachedImageUri));
                        postData.put("Latitude", latitude);
                        postData.put("Longitude", longitude);
                        postData.put("Description",description);
                        postData.put("UserID", String.valueOf(userid));
                        new Post_ads.SendDeviceDetails().execute(Urls.url+Urls.postads, postData.toString());
                        Log.e("values",""+postData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                removepic.setVisibility(View.GONE);
                uploadimage.setVisibility(View.VISIBLE);
                change_image_text.setVisibility(View.VISIBLE);
                preview.setVisibility(View.GONE);
            }
        });




        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Post_ads.this);
                alertDialogBuilder.setMessage("Select Image");
                alertDialogBuilder.setPositiveButton("Open Camera",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(Post_ads.this,"Take a photo using camera",Toast.LENGTH_LONG).show();
                        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(i, RESULT_CAM_IMAGE);

                    }
                });


                alertDialogBuilder.setNegativeButton("From gallery",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(Post_ads.this,"upload from gallery",Toast.LENGTH_LONG).show();
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

        postadsbtn.setEnabled(true);
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
        ProgressDialog p=new ProgressDialog(Post_ads.this);
        @Override
        protected void onPreExecute() {
            p.setMessage("loading...");
            p.show();
            postadsbtn.setEnabled(false);

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

            Toast.makeText(Post_ads.this, s, Toast.LENGTH_SHORT).show();
            Log.e("status", s);
            etitle.getText().clear();
            edescription.getText().clear();

            if (s.equals("true"))
            {
                p.dismiss();
                Toast.makeText(Post_ads.this, "Successful", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else if (s.equals("false"))
            {
                p.dismiss();
                Toast.makeText(Post_ads.this, "Not Posted", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(User_Register.this, MainActivity.class));
            }
            else
            {
                Toast.makeText(Post_ads.this, "Network Pblm...!!!", Toast.LENGTH_SHORT).show();
            }
            p.dismiss();
            Log.e("TAG", s); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }


}
