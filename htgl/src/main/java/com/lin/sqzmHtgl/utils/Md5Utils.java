package com.lin.sqzmHtgl.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;

/**
 * @author lin
 */
public class Md5Utils {
    //生成salt
    public static String CretaeMd5(){
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        return salt;
    }
    //加密
    public static String md5Encryption(String password,String salt){
        String password2= DigestUtils.md5Hex(password + salt);
        return password2;
    }
}
