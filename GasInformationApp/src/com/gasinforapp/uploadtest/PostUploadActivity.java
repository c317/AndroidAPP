package com.gasinforapp.uploadtest;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.gasinformationapp_101.R;


public class PostUploadActivity extends Activity {
    private TextView mShowResponse ;
    private ImageView mImageView ;
    private ProgressDialog mDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);
        mShowResponse = (TextView) findViewById(R.id.id_show_response) ;
        mImageView = (ImageView) findViewById(R.id.id_show_img) ;
        mDialog = new ProgressDialog(this) ;
        mDialog.setCanceledOnTouchOutside(false);
    }

    public void uploadImg(View view){
        mDialog.setMessage("图片上传中...");
        mDialog.show();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.gas_icon02) ;
        UploadApi.uploadImg(bitmap,new ResponseListener<String>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("zgy","===========VolleyError========="+error) ;
                mShowResponse.setText("ErrorResponse\n"+error.getMessage());
                Toast.makeText(PostUploadActivity.this,"上传失败",Toast.LENGTH_SHORT).show() ;
                mDialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
                response = response.substring(response.indexOf("img src="));
                response = response.substring(8,response.indexOf("/>")) ;
                Log.v("zgy","===========onResponse========="+response) ;
                mShowResponse.setText("图片地址:\n"+response);
                mDialog.dismiss();
                Toast.makeText(PostUploadActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
            }
        }) ;
    }
}
