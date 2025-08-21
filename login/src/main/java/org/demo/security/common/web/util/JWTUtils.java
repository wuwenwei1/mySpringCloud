package org.demo.security.common.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.demo.security.common.web.exception.ExceptionTool;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {


    }

    /**
     * 验证Token（Jwt）的有效性
     * @return 如果Token有效返回true，否则返回false
     */
    public static Boolean verifyToken(String jwtToken, String publicKeyStr) {
        // 尝试解析Token，如果解析失败则直接返回false
        Claims claims = null;
        try {
            PublicKey publicKey = publicKeyStrConvertObject(publicKeyStr);
            claims = parseToken(jwtToken, publicKey);
        } catch (Exception e) {
            return false;
        }

        // 如果Token解析成功，将其转换为Map对象以便检查过期时间
        if (claims != null) {
            Date expiration = claims.getExpiration();
            Date nowTime = new Date();
            if(expiration.before(nowTime)){
                return false;
            }else if(expiration.after(nowTime)){
                return true;
            }else{

                return true;
            }
        }
        return false;
    }



    /**
     * 解析JWT令牌，获取其中的主体信息
     */
    public static Claims parseToken(String jwtToken, PublicKey publicKey) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwtToken)
                .getBody();

    }


    /**
     * 创建JWT令牌
     */
    public static String createToken(Map<String, Object> claimsMap,PrivateKey privateKey) {
        long nowMillis = System.currentTimeMillis();
        // 签发时间
        Date now = new Date(nowMillis);
        // 有效期
        Date expDate = new Date(nowMillis+50000);
        return Jwts.builder()
                .setClaims(claimsMap)
                .signWith(privateKey)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .compact();
    }

    /**
     * 生成密匙对
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        // 初始化密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 设置密钥大小为2048位
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 将私匙字符串转成私匙对象
     * @param privateKeyStr
     * @return
     */
    public static PrivateKey privateKeyStrConvertObject(String privateKeyStr) {
        try {
            // 利用JDK自带的工具生成私钥
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(privateKeyStr));
            return kf.generatePrivate(ks);
        } catch (Exception e) {

            ExceptionTool.throwException("获取Jwt私钥失败","5000");
            return null;
        }
    }

    /**
     * 将公匙字符串转成公匙对象
     * @param publicKeyStr
     * @return
     */
    public static PublicKey publicKeyStrConvertObject(String publicKeyStr) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            // 获取公钥失败
            ExceptionTool.throwException("获取Jwt公钥失败","5000");
            return null;
        }
    }


    /**
     * 将公钥转换为字符串
     * @param keyPair
     * @return
     */
    public static String getPublicKeyStr(KeyPair keyPair)  {
        // 提取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 将公钥转换为字符串
        byte[] encoded = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }


    /**
     * 将私钥转换为字符串
     * @param keyPair
     * @return
     */
    public static String getPrivateKeyStr(KeyPair keyPair)  {
        // 提取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //
        byte[] encoded = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }
}
