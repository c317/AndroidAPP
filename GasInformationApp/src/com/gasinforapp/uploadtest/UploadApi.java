package com.gasinforapp.uploadtest;


import android.graphics.Bitmap;

import com.android.volley.Request;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by moon.zhong on 2015/3/2.
 */
public class UploadApi {

    /**
     * 上传图片接口
     * @param bitmap 需要上传的图片
     * @param listener 请求回调
     */
    public static void uploadImg(Bitmap bitmap,ResponseListener listener){
        List<FormFile> imageList = new ArrayList<FormFile>() ;
        imageList.add(new FormFile("测试.png",1,bitmap)) ;
        Request request = new PostUploadRequest(MyConfig.SERVER_URL_UPLOAD+MyConfig.ACTION_FILE_UPLOAD,imageList,listener) ;
        VolleyUtil.getRequestQueue().add(request);;
    }
    public static void uploadImg(File file,ResponseListener listener){
    	List<FormFile> filelist = new ArrayList<FormFile>() ;
    	filelist.add(new FormFile(file.getName(),1,file)) ;
    	Request request = new PostUploadRequest(MyConfig.SERVER_URL_UPLOAD+MyConfig.ACTION_FILE_UPLOAD,filelist,listener) ;
    	VolleyUtil.getRequestQueue().add(request);;
    }
}
