package com.rjhc.credit.information.service.server.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName User
 * @Description:  用户表
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sc_login_user")
public class User {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 登录ID
     */
    @TableField(value = "LOGIN_ID")
    private String loginId;

    /**
     * 登录名
     */
    @TableField("LOGIN_NAME")
    private String loginName;

    /**
     *密码
     */
    @TableField("PASS_WORD")
    private String password;

    /**
     * 用户类型
     */
    @TableField("USER_TYPE")
    private Integer userType;


    /**
     * 机构编码
     */
    @TableField("PBC_CODE")
    private String pbcCode;

    /**
     * 机构名称
     */
    @TableField("PBC_NAME")
    private String pbcName;

    /**
     * 机构名称
     */
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;
}
