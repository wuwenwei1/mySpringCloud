package org.demo.security.authentication.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.demo.security.authentication.handler.login.LoginUser;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.util.IdUtils;
import org.demo.security.common.web.util.JWTUtils;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.common.constant.CacheConstants;
import org.demo.security.common.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 公钥秘匙
    @Value("${token.publicKeyStr}")
    private String publicKeyStr;

    // 私钥秘匙
    @Value("${token.privateKeyStr}")
    private String privateKeyStr;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;



    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try{
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                return user;
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    ExceptionTool.throwException("系统错误","5000");
                }else if(e instanceof SignatureException){
                    ExceptionTool.throwException("无效的令牌","6000");
                }else {
                    ExceptionTool.throwException("登录过期","6000");
                }
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        try {
            loginUser.setLoginTime(System.currentTimeMillis());
            loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
            // 根据uuid将loginUser缓存
            String userKey = getTokenKey(loginUser.getToken());

            redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        }catch (Exception e){
            ExceptionTool.throwException("系统错误","5000");
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            try {
                String userKey = getTokenKey(token);
                redisCache.deleteObject(userKey);
            }catch (Exception e){
                ExceptionTool.throwException("系统错误","5000");
            }
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        PrivateKey privateKey = JWTUtils.privateKeyStrConvertObject(privateKeyStr);
        String token =Jwts.builder()
                .setClaims(claims)
                .signWith(privateKey)
                .compact();
        return token;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }





    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        PublicKey publicKey = JWTUtils.publicKeyStrConvertObject(publicKeyStr);
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)){
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }else {
            ExceptionTool.throwException("JWT token is missing!", "5000");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
