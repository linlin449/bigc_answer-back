package com.lin.bigc_answer.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {//过期时间
    private static final long EXPIRE_TIME = 3 * 60 * 60 * 1000;//默认3天
    //私钥
    private static final String TOKEN_SECRET = "th1nkkkkkkkkkkkkkkkkkkkk";

    /**
     * 根据username生成token
     * @param username 用户名
     * @param userRole 用户角色
     * @return token字符串
     */
    public static String createToken(String username, UserRole userRole) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("role", userRole.name())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据username,自定义过期时间生成token
     * @param username 用户名
     * @param userRole 用户角色
     * @param expireDate 过期时间(毫秒)
     * @return token字符串
     */
    public static String createToken(String username, UserRole userRole, long expireDate) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + expireDate);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("role", userRole.name())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从token中获取username
     * @param token token字符串
     * @return 成功返回token信息中的username, 失败返回null
     */
    public static String getUserName(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("username").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中获取userrole
     * @param token token字符串
     * @return 成功返回token信息中的userRole, 失败返回null
     */
    public static UserRole getUserRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return UserRole.valueOf(jwt.getClaim("role").asString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据当前时间与默认过期时间生成过期时间戳
     * @return 过期时间戳
     */
    public static long getExpireTime() {
        return System.currentTimeMillis() + EXPIRE_TIME;
    }

    /**
     * 根据当前时间与提供的过期时间生成过期时间戳
     * @param expireDate 过期时间(毫秒)
     * @return 过期时间戳
     */
    public static long getExpireTime(long expireDate) {
        return System.currentTimeMillis() + EXPIRE_TIME;
    }
}