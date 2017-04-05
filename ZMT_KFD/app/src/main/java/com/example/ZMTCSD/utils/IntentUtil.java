package com.example.ZMTCSD.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.apkfuns.logutils.LogUtils;

import java.io.File;

/**
 * 以下是打开各种文件的intent
 * Created by 汤军平 on 2016/7/14.
 */
public class IntentUtil {

    public static int  FileWhatType(String TypeName){
        int type=-1;
        switch (TypeName ){
            case "jpg":
            case "jpeg":
            case "gif":
            case "png":
            case "bmp":
                type= 0;
                break;
            case "xls":
            case "xlxt":
            case "xlsx":
                type= 1;
                break;
            case "txt":
            case "doc":
            case "word":
            case "docx":
                type= 2;
                break;
            case "pdf":
                type= 3;
                break;
            case "rar":
            case "zip":
                type= 4;
                break;
            case "html":
                type=5;
                break;
            default:
                type= -1;
                break;
        }
        return type;
    }

    public static void OpenFile(Context mContext, String fileType , String param){
        switch (fileType){
            case "jpg":
            case "jpeg":
            case "gif":
            case "png":
                mContext.startActivity( getImageFileIntent(param) );
                break;
            case "xls":
            case "xlxt":
            case "xlsx":
                mContext.startActivity(getExcelFileIntent(param));
                break;
            case "txt":
                mContext.startActivity(getTextFileIntent(param,false));
                break;
            case "doc":
            case "word":
            case "docx":
                mContext.startActivity(getWordFileIntent(param));
                break;
            case "pdf":
                mContext.startActivity(getPdfFileIntent(param));
                break;
            case "ppt":
                mContext.startActivity (getPptFileIntent(param));
                break;
            case "rar":
            case "zip":
                mContext.startActivity (getAllIntent(param));
                break;
            default:
                mContext.startActivity (getAllIntent(param));
                break;
        }

    }
    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent( String param ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri,"*/*");
        return intent;
    }
    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent( String param ) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO 视频文件文件的intent
    public static Intent getVideoFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent( String param ){

        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent( String param, boolean paramBoolean){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean){
            Uri uri1 = Uri.parse(param );
            intent.setDataAndType(uri1, "text/plain");
        }else{
            Uri uri2 = Uri.fromFile(new File(param ));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

//    //下面这些都OK
//Intent it = getHtmlFileIntent("/mnt/sdcard/tutorial.html");//SD卡主目录
    //Intent it = getHtmlFileIntent("/sdcard/tutorial.html");//SD卡主目录,这样也可以
//Intent it = getHtmlFileIntent("/system/etc/tutorial.html");//系统内部的etc目录
//Intent it = getPdfFileIntent("/system/etc/helphelp.pdf");
//Intent it = getWordFileIntent("/system/etc/help.doc");
//Intent it = getExcelFileIntent("/mnt/sdcard/Book1.xls")
//Intent it = getPptFileIntent("/mnt/sdcard/download/Android_PPT.ppt");//SD卡的download目录下
//Intent it = getVideoFileIntent("/mnt/sdcard/ice.avi");
//Intent it = getAudioFileIntent("/mnt/sdcard/ren.mp3");
//Intent it = getImageFileIntent("/mnt/sdcard/images/001041580.jpg");
//Intent it = getTextFileIntent("/mnt/sdcard/hello.txt",false);
//    startActivity( it );

}
