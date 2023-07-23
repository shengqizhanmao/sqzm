package com.lin.common;

import org.jetbrains.annotations.NotNull;

/**
 * @author lin
 */
public class RedisStatus {
    //存储认证权限
    public static final String INFO_TOKEN = "Info:Token:";
    public static final String INFO_SUSERREALM = "Info:SUserRealm:";
    //存储token
    public static final String TOKEN_SUser = "Token:SUser:";
    public static final String TOKEN_USER = "Token:User:";
    //存储User登录的code
    public static final String USER_CODE = "Code:User:";
    //存储User登录的邮箱code
    public static final String USER_EMAIL_CODE = "Code:User:Email:";
    //修改用户邮箱的code
    public static final String USER_UPDATE_EMAIL_CODE = "Code:User:Update:Email:";
    //查看文章的用户,十分钟,view不改变
    public static final String ARTCILE_USER_VIEW_TOKEN = "Article:User:View:Token:";

}
