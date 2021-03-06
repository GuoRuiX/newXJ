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
     * ??????
     */
    @Value("${jwt.secret.key}")
    private String secretKey;
    /**
     * token????????????
     */
    @Value("${jwt.token.expire.time}")
    private long tokenExpireTime;
    /**
     * #?????? ??????key
     */
    @Value("${jwt.single.key.prefix}")
    private String singleKeyPrefix;
    /**
     * #refreshToken ??????key
     */
    @Value("${jwt.refresh.token.key.prefix}")
    private String jwtRefreshTokenKeyPrefix;
    /**
     * #token????????? ??????key
     */
    @Value("${jwt.blacklist.key.prefix}")
    private String jwtBlacklistKeyPrefix;
    /**
     * refreshToken????????????
     */
    @Value("${jwt.refresh.token.expire.time}")
    private long refreshTokenExpireTime;
    /**
     * #token????????? ??????key
     */
    @Value("${jwt.yellowlist.key.prefix}")
    private String jwtYellowlistKeyPrefix;
    /**
     * ????????????token????????????
     */
    @Value("${jwt.yellowlist.expire.time}")
    private long yellowlistExpireTime;
    @Autowired
    private RequestUtil requestUtil;

    @OptionalLog(module = "??????",operations = "??????")
    @Override
    public RestfulApiResponse<UserDto> queryAuditRecords(UserParam userParam, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        UserDto userDto = userService.selUserNameAndPsd(userParam);
        if(userDto != null){//????????????????????????session
            httpSession.setAttribute("username",userDto.getLoginName());
            httpSession.setAttribute("password",userDto.getPassword());
            httpSession.setAttribute("userType",userDto.getUserType());
            httpSession.setAttribute("pbcCode",userDto.getPbcCode());
            httpSession.setAttribute("pbcName",userDto.getPbcName());
        }
        return RestfulApiResponse.success(userDto);
    }
    /**
     * ???????????????
     * ??????????????????
     * @Author: grx
     * @Date: ??????2:37 2021/1/15
     * @param authUserParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.rjhc.credit.information.service.api.model.dto.UserDto>
     */
    @Override
    @OptionalLog(module = "??????",operations = "??????")
    public RestfulApiResponse<UserDto> login(AuthUserParam authUserParam) throws Exception {
        String username = authUserParam.getUserName();
        String password = authUserParam.getPassWord();
        //???????????????????????????????????????
        Map<String, Object> userandUserName = getXiAnData.getUserandUserName(username);
        log.info("?????????????????????"+userandUserName.toString());
        log.info("?????????????????????????????????"+userandUserName.get("loginName"));
        BizExceptionEnum.USER_NO_EXISTS.notNullAssert(userandUserName);
        String encrypt = SM3Utils.encrypt(password);
        String s = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("?????????????????????????????????"+ s);
        if(!s.equals(userandUserName.get("passWord"))){
            throw new BizException(BizExceptionEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), BizExceptionEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
        }
        String token = buildJWT(userandUserName.get("loginId").toString());
        //??????refreshToken
        String refreshToken = RandomUtil.UUID32();
        String id = userandUserName.get("loginId").toString();
        //????????????????????????????????????????????????
        String roleName = getXiAnData.getUserRelo(userandUserName.get("loginId").toString());
        userandUserName.put("roleName",roleName);
        singleLogin(id,refreshToken);
        //??????refreshToken???redis?????????hash????????????????????????token??????????????????
        String refreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, refreshToken);
        redisUtil.hPut(refreshTokenKey, TokenRedisConstant.TOKEN, token);
        redisUtil.hPut(refreshTokenKey, TokenRedisConstant.USERINFO, JSON.toJSONString(userandUserName));
        redisUtil.expire(refreshTokenKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
        return RestfulApiResponse.success(new AuthDto(token, refreshToken,userandUserName));
    }

    @Override
    public RestfulApiResponse<UserDto> refreshToken() {
        //????????????token
        String curToken = requestUtil.getHeader(RequestHeadConstant.TOKEN);
        //?????????token???????????????
        redisUtil.setEx(String.format(jwtYellowlistKeyPrefix, curToken), "",
                yellowlistExpireTime, TimeUnit.MILLISECONDS);

        //???header?????? ??????refreshToken
        String refreshToken = requestUtil.getHeader(RequestHeadConstant.REFRESH_TOKEN);
        String refreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, refreshToken);
        String userInfo = (String) redisUtil.hGet(refreshTokenKey, TokenRedisConstant.USERINFO);
        //?????? redis???userId????????????
        CommonExceptionEnum.JWT_REFRESH_TOKEN_INVALID.notNullAssert(userInfo);

        String newToken = "";
        Map<String, Object> userDto = new HashMap<>();
        if(StringUtil.isNotNull(userInfo)){
             userDto = JSONArray.parseObject(userInfo, Map.class);
            //?????????token
            String oldToken = (String) redisUtil.hGet(refreshTokenKey, TokenRedisConstant.TOKEN);
            //???oldToken???????????????
            redisUtil.setEx(String.format(jwtYellowlistKeyPrefix, oldToken), "",
                    yellowlistExpireTime, TimeUnit.MILLISECONDS);
            //??????token??????????????????
            redisUtil.setEx(String.format(jwtBlacklistKeyPrefix, oldToken), "",
                    tokenExpireTime, TimeUnit.MILLISECONDS);

            //????????????token
            newToken = buildJWT(userDto.get("loginId").toString());
            redisUtil.hPut(refreshTokenKey, TokenRedisConstant.TOKEN, newToken);
            //????????????????????????
            redisUtil.expire(refreshTokenKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
        }
        return RestfulApiResponse.success(new AuthDto(newToken, refreshToken,userDto));
    }

    @Override
    public RestfulApiResponse loginOut() {
        String token = requestUtil.getHeader(TokenRedisConstant.TOKEN);
        //??????token??????????????????
        redisUtil.setEx(String.format(jwtBlacklistKeyPrefix, token), "", tokenExpireTime, TimeUnit.MILLISECONDS);
        return RestfulApiResponse.success();
    }

    /**
     * ??????jwt token
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
     * ???????????????????????????
     * ????????????????????????????????????????????????
     * ???????????????
     * ????????????token???????????????
     * ???refreshToken??????redis???????????????????????????
     * ????????????????????????
     * ????????????userId,refreshToken??????redis????????????????????????
     * @Author: grx
     * @Date: ??????5:33 2021/1/15
     * @param userId
     * @param refreshToken
     * @return: void
     */
    private void singleLogin(String userId,String refreshToken){
        //?????????key
        String singleKeyPrefixKey = String.format(singleKeyPrefix, userId);
        //?????????????????????????????????
        if(redisUtil.hasKey(singleKeyPrefixKey)){
            String beforeRefreshToken = (String) redisUtil.hGet(singleKeyPrefixKey, TokenRedisConstant.REFRESHTOKEN);
            if(StringUtil.isNotNull(beforeRefreshToken)){
                //????????????????????????token???key
                String beforeRefreshTokenKey = String.format(jwtRefreshTokenKeyPrefix, beforeRefreshToken);
                if(redisUtil.hasKey(beforeRefreshTokenKey)){
                    String beforeToken = (String) redisUtil.hGet(beforeRefreshTokenKey,TokenRedisConstant.TOKEN);
                    //????????????token???????????????
                    redisUtil.setEx(String.format(jwtBlacklistKeyPrefix, beforeToken), "",
                            tokenExpireTime, TimeUnit.MILLISECONDS);
                    //??????????????????token??????????????????
                    redisUtil.expire(beforeRefreshTokenKey, 0, TimeUnit.MILLISECONDS);
                }
            }
        }
        //?????????id???key?????????token???value?????????redis
        redisUtil.hPut(singleKeyPrefixKey, TokenRedisConstant.REFRESHTOKEN, refreshToken);
        //????????????
        redisUtil.expire(singleKeyPrefixKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }

}
