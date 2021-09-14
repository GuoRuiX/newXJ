package com.rjhc.credit.information.service.start.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rjhc.matrix.framework.core.constant.RequestHeadConstant;
import com.rjhc.matrix.framework.core.constant.TokenRedisConstant;
import com.rjhc.matrix.framework.core.enums.CommonExceptionEnum;
import com.rjhc.matrix.framework.core.exception.BizExceptionAssert;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import com.rjhc.matrix.framework.core.util.RedisUtil;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;


/**
 * @Description: 全局过滤器
 * @Author: shuaichao gao
 * @Date: 2020/7/29 14:28
 * @return: null
 **/
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "globalFilter")
@Component
public class GlobalFilter implements Filter {

    /**
     * jwt的key
     **/
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * #refreshToken 存储key
     */
    @Value("${jwt.refresh.token.key.prefix}")
    private String jwtRefreshTokenKeyPrefix;

    /**
     * token的黑名单前缀
     **/
    @Value("${jwt.blacklist.key.prefix}")
    private String jwtBlacklistKeyPrefix;

    /**
     * #token黄名单 存储key
     */
    @Value("${jwt.yellowlist.key.prefix}")
    private String jwtYellowlistKeyPrefix;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RequestUtil requestUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();

        //验签，注意获取验证码、登录、swagger等接口不用验签
        if (!uri.contains("swagger") && !uri.contains("v2/api-docs")
                && !uri.contains("verificationCode") && !uri.contains("login")&& !uri.contains("insertTestPoor")) {
            //从请求头中取出token
            String token = request.getHeader(RequestHeadConstant.TOKEN);
            if(uri.contains("selectPoorPDF")){
                String[] split = uri.split("\\/");
                token=split[split.length-2];
            }
            //验证是否携带token
            if(StringUtil.isNull(token)){
                exceptionResponse(response, CommonExceptionEnum.JWT_TOKEN_MISSION);
                return;
            }
            //验证token是否在黄名单和黑名单内
            if(isYellowBlackToken(token)){
                exceptionResponse(response, CommonExceptionEnum.JWT_TOKEN_INVALID);
                return;
            }
            //验签
            DecodedJWT decodedJWT = null;
            try{
                decodedJWT = verifyToken(token,secretKey);
            } catch (JWTDecodeException jwtDecodeException) {
                exceptionResponse(response, CommonExceptionEnum.JWT_TOKEN_INVALID);
                return;
            } catch (SignatureVerificationException signatureVerificationException) {
                exceptionResponse(response, CommonExceptionEnum.JWT_SIGNATURE_INVALID);
                return;
            } catch (TokenExpiredException tokenExpiredException) {
                exceptionResponse(response, CommonExceptionEnum.JWT_TOKEN_EXPIRED);
                return;
            } catch (Exception ex) {
                exceptionResponse(response, CommonExceptionEnum.UNKNKOWN);
                return;
            }

            //获取当前userId
            String userId = decodedJWT.getClaim(RequestHeadConstant.USERID).asString();
            //在request中设置userId的header
            setParam2Header(request,response,RequestHeadConstant.USERID, userId);

            //在request中设置account的header
            String refreshToken = requestUtil.getHeader(RequestHeadConstant.REFRESH_TOKEN);
            String refreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, refreshToken);
            String userInfo = (String) redisUtil.hGet(refreshTokenKey, TokenRedisConstant.USERINFO);
            if(StringUtil.isNotNull(userInfo)){
                Map<String,Object> map = JSONArray.parseObject(userInfo, Map.class);
                //在request中设置userId的header
                setParam2Header(request,response,RequestHeadConstant.ACCOUNT, map.get("loginName").toString());
            }


        }
        filterChain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

    /**
     * 异常直接response
     *
     * @param response
     * @param responseCodeEnum
     * @return
     */
    private void exceptionResponse(HttpServletResponse response, BizExceptionAssert responseCodeEnum) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        //获取输出流
        OutputStream outputStream = response.getOutputStream();
        //使用OutputStream流向客户端输出字节数组
        outputStream.write(JSON.toJSONString(RestfulApiResponse.error(responseCodeEnum)).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 判断token是否在黄名单黑名单内
     * @param token
     * @return
     */
    private boolean isYellowBlackToken(String token){
        boolean flag = false;
        //如果token在黄名单里,在一定时间内此旧token依然可以使用
        if(!stringRedisTemplate.hasKey(String.format(jwtYellowlistKeyPrefix, token)) && stringRedisTemplate.hasKey(String.format(jwtBlacklistKeyPrefix, token))){
            flag = true;
        }
        return flag;
    }

    /**
     * 验签Token
     *
     * @param token
     * @param secretKey
     */
    private DecodedJWT verifyToken(String token, String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("RJHC").build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }

    /**
     * @Description: 设置header
     * @Author: shuaichao gao
     * @Date: 2020/7/29 17:55
     * @param request
     * @param response
     * @param key
     * @param value
     * @return: void
     **/
    private void setParam2Header(HttpServletRequest request, HttpServletResponse response, String key, String value) throws IOException {

        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field request1 = requestClass.getDeclaredField("request");
            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders) headers.get(o1);
            o2.addValue(key).setString(value);
        } catch (Exception e) {
            exceptionResponse(response,CommonExceptionEnum.REQUEST_HEADER_FAIL);
        }

    }
}
