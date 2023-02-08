package com.lin.common;

/**
 * @author lin
 */
public class RedisStatus {
    //存储认证权限
    public  static final String INFO_TOKEN=getInfoToken();
    private static String getInfoToken(){
        return "Info:Token:";
    }
    //存储token
    public static final String TOKEN="Token:";
}
