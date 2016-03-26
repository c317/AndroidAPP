package com.gasinforapp.uploadtest;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;


public class FormImage {
    //参数的名称
    private String mName ;
    //文件名
    private String mFileName ;
    //文件的 mime，需要根据文档查询
    private String mMime ;
    //需要上传的图片资源，因为这里测试为了方便起见，直接把 bigmap 传进来，真正在项目中一般不会这般做，而是把图片的路径传过来，在这里对图片进行二进制转换
    private Bitmap mBitmap ;

    public FormImage(String mName,String mFileName,int mfileType,Bitmap mBitmap) {
    	this.mName = mName;
    	this.mFileName = mFileName;
    	if(mfileType == 1){
    		this.mMime = "image/png";
    	}else{
    		this.mMime = "application/msword";
    	}
        this.mBitmap = mBitmap;
    }

    public String getName() {
        return mName;
    }

    public String getFileName() {
    	return mFileName;
    }
    //对图片进行二进制转换
    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        mBitmap.compress(Bitmap.CompressFormat.JPEG,80,bos) ;
        return bos.toByteArray();
    }

    public String getMime() {
        return mMime;
    }
}