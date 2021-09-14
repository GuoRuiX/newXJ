package com.rjhc.credit.information.service.start.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.LoginInterface;
import com.rjhc.credit.information.service.api.model.dto.AuthDto;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.AuthUserParam;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.credit.information.service.server.common.enums.BizExceptionEnum;
import com.rjhc.credit.information.service.server.dao.dataobject.User;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.middleware.SM3Utils;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.UserService;
import com.rjhc.matrix.framework.core.constant.RequestHeadConstant;
import com.rjhc.matrix.framework.core.constant.TokenRedisConstant;
import com.rjhc.matrix.framework.core.enums.CommonExceptionEnum;
import com.rjhc.matrix.framework.core.exception.bean.BizException;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import com.rjhc.matrix.framework.core.util.RedisUtil;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/18
 * @Version V1.0
 **/
@RestController
@Slf4j
public class LoginController implements LoginInterface {
    private static final String ENCODING = "UTF-8";
    @Autowired
    private UserService userService;
    @Autowired
    private GetXiAnData getXiAnData;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 密钥
     */
    @Value("${jwt.secret.key}")
    private String secretKey;
    /**
     * token超时时间
     */
    @Value("${jwt.token.expire.time}")
    private long tokenExpireTime;
    /**
     * #单点 存储key
     */
    @Value("${jwt.single.key.prefix}")
    private String singleKeyPrefix;
    /**
     * #refreshToken 存储key
     */
    @Value("${jwt.refresh.token.key.prefix}")
    private String jwtRefreshTokenKeyPrefix;
    /**
     * #token黑名单 存储key
     */
    @Value("${jwt.blacklist.key.prefix}")
    private String jwtBlacklistKeyPrefix;
    /**
     * refreshToken过期时间
     */
    @Value("${jwt.refresh.token.expire.time}")
    private long refreshTokenExpireTime;
    /**
     * #token黄名单 存储key
     */
    @Value("${jwt.yellowlist.key.prefix}")
    private String jwtYellowlistKeyPrefix;
    /**
     * 黄名单里token超时时间
     */
    @Value("${jwt.yellowlist.expire.time}")
    private long yellowlistExpireTime;
    @Autowired
    private RequestUtil requestUtil;

    @OptionalLog(module = "登录",operations = "登录")
    @Override
    public RestfulApiResponse<UserDto> queryAuditRecords(UserParam userParam, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        UserDto userDto = userService.selUserNameAndPsd(userParam);
        if(userDto != null){//将用户信息存贮到session
            httpSession.setAttribute("username",userDto.getLoginName());
            httpSession.setAttribute("password",userDto.getPassword());
            httpSession.setAttribute("userType",userDto.getUserType());
            httpSession.setAttribute("pbcCode",userDto.getPbcCode());
            httpSession.setAttribute("pbcName",userDto.getPbcName());
        }
        return RestfulApiResponse.success(userDto);
    }
    /**
     * 功能描述：
     * 〈用户登陆〉
     * @Author: grx
     * @Date: 下午2:37 2021/1/15
     * @param authUserParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.rjhc.credit.information.service.api.model.dto.UserDto>
     */
    @Override
    @OptionalLog(module = "登录",operations = "登录")
    public RestfulApiResponse<UserDto> login(AuthUserParam authUserParam) throws Exception {
        String username = authUserParam.getUserName();
        String password = authUserParam.getPassWord();
        //获取西安数据库用户是否存在
        Map<String, Object> userandUserName = getXiAnData.getUserandUserName(username);
        log.info("当前用户信息为"+userandUserName.toString());
        log.info("当前登陆用户，用户名："+userandUserName.get("loginName"));
        BizExceptionEnum.USER_NO_EXISTS.notNullAssert(userandUserName);
        String encrypt = SM3Utils.encrypt(password);
        String s = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("系统自行加密后的密码："+ s);
        if(!s.equals(userandUserName.get("passWord"))){
            throw new BizException(BizExceptionEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), BizExceptionEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
        }
        String token = buildJWT(userandUserName.get("loginId").toString());
        //生成refreshToken
        String refreshToken = RandomUtil.UUID32();
        String id = userandUserName.get("loginId").toString();
        singleLogin(id,refreshToken);
        //保存refreshToken至redis，使用hash结构保存使用中的token以及用户标识
        String refreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, refreshToken);
        redisUtil.hPut(refreshTokenKey, TokenRedisConstant.TOKEN, token);
        redisUtil.hPut(refreshTokenKey, TokenRedisConstant.USERINFO, JSON.toJSONString(userandUserName));
        redisUtil.expire(refreshTokenKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
        return RestfulApiResponse.success(new AuthDto(token, refreshToken,userandUserName));
    }

    @Override
    public RestfulApiResponse<UserDto> refreshToken() {
        //获取当前token
        String curToken = requestUtil.getHeader(RequestHeadConstant.TOKEN);
        //将当前token放入黄名单
        redisUtil.setEx(String.format(jwtYellowlistKeyPrefix, curToken), "",
                yellowlistExpireTime, TimeUnit.MILLISECONDS);

        //从header获取 当前refreshToken
        String refreshToken = requestUtil.getHeader(RequestHeadConstant.REFRESH_TOKEN);
        String refreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, refreshToken);
        String userInfo = (String) redisUtil.hGet(refreshTokenKey, TokenRedisConstant.USERINFO);
        //效验 redis中userId是否过期
        CommonExceptionEnum.JWT_REFRESH_TOKEN_INVALID.notNullAssert(userInfo);

        String newToken = "";
        Map<String, Object> userDto = new HashMap<>();
        if(StringUtil.isNotNull(userInfo)){
             userDto = JSONArray.parseObject(userInfo, Map.class);
            //取出旧token
            String oldToken = (String) redisUtil.hGet(refreshTokenKey, TokenRedisConstant.TOKEN);
            //将oldToken放入黄名单
            redisUtil.setEx(String.format(jwtYellowlistKeyPrefix, oldToken), "",
                    yellowlistExpireTime, TimeUnit.MILLISECONDS);
            //将旧token添加到黑名单
            redisUtil.setEx(String.format(jwtBlacklistKeyPrefix, oldToken), "",
                    tokenExpireTime, TimeUnit.MILLISECONDS);

            //生成新的token
            newToken = buildJWT(userDto.get("loginId").toString());
            redisUtil.hPut(refreshTokenKey, TokenRedisConstant.TOKEN, newToken);
            //重新设置超时时间
            redisUtil.expire(refreshTokenKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
        }
        return RestfulApiResponse.success(new AuthDto(newToken, refreshToken,userDto));
    }

    @Override
    public RestfulApiResponse loginOut() {
        String token = requestUtil.getHeader(TokenRedisConstant.TOKEN);
        //将旧token添加到黑名单
        redisUtil.setEx(String.format(jwtBlacklistKeyPrefix, token), "", tokenExpireTime, TimeUnit.MILLISECONDS);
        return RestfulApiResponse.success();
    }

    /**
     * 生成jwt token
     *
     * @param userId
     * @return
     */
    private String buildJWT(String userId) {
        Date now = new Date();
        Algorithm algo = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withIssuer("RJHC")
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + tokenExpireTime))
                .withClaim(RequestHeadConstant.USERID, userId)
                .withClaim("random", RandomUtil.UUID32())
                .sign(algo);
        return token;
    }
    /**
     * 功能描述：单点登陆
     * 〈验证该用户是否在其他地方登陆〉
     * 若已登录：
     * 将之前的token放入黑名单
     * 将refreshToken存在redis里的信息设置为过期
     * 不管是否已登录：
     * 将当前的userId,refreshToken存入redis，并设置超时时间
     * @Author: grx
     * @Date: 下午5:33 2021/1/15
     * @param userId
     * @param refreshToken
     * @return: void
     */
    private void singleLogin(String userId,String refreshToken){
        //单点的key
        String singleKeyPrefixKey = String.format(singleKeyPrefix, userId);
        //判断该用户是否已登录过
        if(redisUtil.hasKey(singleKeyPrefixKey)){
            String beforeRefreshToken = (String) redisUtil.hGet(singleKeyPrefixKey, TokenRedisConstant.REFRESHTOKEN);
            if(StringUtil.isNotNull(beforeRefreshToken)){
                //之前登录后的刷新token的key
                String beforeRefreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, beforeRefreshToken);
                if(redisUtil.hasKey(beforeRefreshTokenKey)){
                    String beforeToken = (String) redisUtil.hGet(beforeRefreshTokenKey,TokenRedisConstant.TOKEN);
                    //将之前的token放入黑名单
                    redisUtil.setEx(String.format(jwtBlacklistKeyPrefix, beforeToken), "",
                            tokenExpireTime, TimeUnit.MILLISECONDS);
                    //将之前的刷新token，设置成过期
                    redisUtil.expire(beforeRefreshTokenKey, 0, TimeUnit.MILLISECONDS);
                }
            }
        }
        //以用户id为key，刷新token为value，存入redis
        redisUtil.hPut(singleKeyPrefixKey, TokenRedisConstant.REFRESHTOKEN, refreshToken);
        //设置超时
        redisUtil.expire(singleKeyPrefixKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }

}
