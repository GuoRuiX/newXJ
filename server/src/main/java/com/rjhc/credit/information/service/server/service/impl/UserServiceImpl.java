package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.credit.information.service.server.common.convertor.UserConverTor;
import com.rjhc.credit.information.service.server.common.enums.BizExceptionEnum;
import com.rjhc.credit.information.service.server.dao.dataobject.User;
import com.rjhc.credit.information.service.server.dao.mapper.UserMapper;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.service.UserService;
import com.rjhc.matrix.framework.core.exception.bean.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private GetXiAnData getXiAnData;
    @Value("${analysis.token}")
    private String token;
    @Override
    public UserDto selUserNameAndPsd(UserParam userParam) {
        User user = UserConverTor.INSTANCE.prarmToentity(userParam);
        User name = new User();
        name.setLoginName(user.getLoginName());
        //根据用户名查当前用户
        UserDto userName = userMapper.selUserNameAndPsd(name);
        if(userName == null){
            throw new BizException(BizExceptionEnum.USER_NO_EXISTS.getCode(), BizExceptionEnum.USER_NO_EXISTS.getMsg());
        }
        name.setPassword(user.getPassword());
        UserDto userPassword = userMapper.selUserNameAndPsd(name);//根据用户名密码查询当前用户
        if(userPassword == null){
            throw new BizException(BizExceptionEnum.USER_PASSWORD_NO_EXISTS.getCode(), BizExceptionEnum.USER_PASSWORD_NO_EXISTS.getMsg());
        }
//        UserDto userDto = UserConverTor.INSTANCE.entityToDto(user);
        return userPassword;
    }

    @Override
    public Page<User> selectByPage(UserParam userParam) {
        Integer currentPageNum = userParam.getCurrentPageNum();
        Integer pageSize = userParam.getPageSize();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //根据当前用户类型查询当前用户展示信息
        if(userParam.getUserType() != null){
            queryWrapper.eq(User::getUserType,userParam.getUserType());
        }
        Page<User> userPage = userMapper.selectPage(new Page<User>(currentPageNum, pageSize), queryWrapper);
        return userPage;
    }
}
