package com.example.splider.utils;

/**
 * Created by Administrator on 2016/7/24.
 */
public class TextUtil {
    public static boolean isEmpty(String str){
        if(str==null || str.trim().length()==0){
            return true;
        }
        return false;
    }
}
