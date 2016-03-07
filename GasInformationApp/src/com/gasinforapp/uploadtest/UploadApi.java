package com.gasinforapp.uploadtest;


import android.graphics.Bitmap;

import com.android.volley.Request;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

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
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(new FormImage(null,null,1,bitmap)) ;
        Request request = new PostUploadRequest(MyConfig.SERVER_URL_WORK,imageList,listener) ;
        VolleyUtil.getRequestQueue().add(request);;
    }
}
