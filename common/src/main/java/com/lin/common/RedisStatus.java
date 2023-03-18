package com.lin.common;

import org.jetbrains.annotations.NotNull;

/**
 * @author lin
 */
public class RedisStatus {
    //存储认证权限
    public static final String INFO_TOKEN = getInfoToken();

    @NotNull
    private static String getInfoToken() {
        return "Info:Token:";
    }

    //存储token
    public static final String TOKEN_SUser = "Token:SUser:";
    public static final String TOKEN_USER = "Token:User:";
    //存储User登录的code
    public static final String USER_CODE = "User:Code:";
    //存储User登录的邮箱code
    public static final String USER_EMAIL_CODE = "User:Email:code:";
    public static final String USER_UPDATE_EMAIL_CODE = "User:Update:Email:code:";
}
