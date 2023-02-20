package com.lin.common;

/**
 * @author lin
 */
public class ResultCode {
    //成功
    public static final int SUCCESS=200;
    //失败
    public static final int FAIL=400;
    //没有权限访问资源
//    public static final int PERMISSION_NO=401;
    //访问资源的权限不够
    public static final int PERMISSION_INSUFFICIENT=403;
    //token过期了,请重新登录
    public static final int TOKEN_OVERDUE=420;
    //帐号被禁用
    public static final int USER_DISABLE=430;
    //http请求参数异常
    public static final int HTTP_REQUEST_PARAMETER_EXCEPTION=415;
    //参数错误
    public static final int PARAMETER_ERROR=415;
    //服务器出现错误
    public static final int ERROR=500;
    //图片上传失败
    public static final int IMAGE_UPLOAD_FAIL=501;
    //删除用户拥有角色失败
    public static final int USER_ROLE_DELETE_FAIL=502;

}
