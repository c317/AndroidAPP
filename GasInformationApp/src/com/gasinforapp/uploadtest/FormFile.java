package com.gasinforapp.uploadtest;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.gasinforapp.config.MyConfig;

public class FormFile {
	// 参数的名称
	private String mName = "file";
	// 文件名
	private String mFileName;
	// 文件的 mime，需要根据文档查询
	private String mMime;
	// 需要上传的图片资源
	private Bitmap mBitmap;
	//文件流
	private InputStream inStream;
	//需上传的文件
	private File file;

	public FormFile(String mFileName, int mfileType, File file) {
		this.mFileName = mFileName;
		 this.file = file;
	        try {
	            this.inStream = new FileInputStream(file);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
		if (mfileType == MyConfig.FILETYPE_IMAGE) {
			this.mMime = "image/png";
		} else if(mfileType == MyConfig.FILETYPE_WORD){
			this.mMime = "application/msword";
		}else if(mfileType == MyConfig.FILETYPE_EXCEL){
			this.mMime = "application/vnd.ms-excel";
		}else if(mfileType == MyConfig.FILETYPE_PPT){
			this.mMime = "application/vnd.ms-powerpoint";
		}else 
			this.mMime = "";
	}

	public FormFile(String mFileName, int mfileType, Bitmap mBitmap) {
		this.mFileName = mFileName;
		this.mMime = "image/png";
		this.mBitmap = mBitmap;
	}

	public String getName() {
		return mName;
	}

	public String getFileName() {
		return mFileName;
	}
    public File getFile() {
        return file;
    }
 
    public InputStream getInStream() {
        return inStream;
    }
 
	// 对图片进行二进制转换
	public byte[] getValue() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		return bos.toByteArray();
	}

	public String getMime() {
		return mMime;
	}
}