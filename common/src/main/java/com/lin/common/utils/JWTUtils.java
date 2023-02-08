package com.lin.common.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lin
 */
public class JWTUtils {

    private static String jwtToken="linShengWei";

    //生成token
    public static String createToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) //签名算法,密钥为jwtToken
                .setClaims(claims)  //设置body数据,有唯一
                .setIssuedAt(new Date()) //设置签名时间
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000)); //一天时间
        String token = jwtBuilder.compact();
        return token;
    }

    //检查token
    public static Map<String, Object> checkToken(String token) {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
    }
    //测试
//    public static void main(String[] args) {
//        String token = JWTUtils.createToken("lin");
//        System.out.println(token);
//        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
//        System.out.println(stringObjectMap);
//    }
}
