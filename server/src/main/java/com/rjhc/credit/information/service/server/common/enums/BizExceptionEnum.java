package com.rjhc.credit.information.service.server.common.enums;

import com.rjhc.matrix.framework.core.exception.BizExceptionAssert;

public enum BizExceptionEnum implements BizExceptionAssert {
    USER_NO_EXISTS("06001","当前用户不存在"),
    USERNAME_OR_PASSWORD_ERROR("06002", "用户名或密码错误"),
    USER_PASSWORD_NO_EXISTS("06119","密码错误"),
    EXPORT_EXCEL("06120","请仔细检查模版数据是否正确"),
    EXPORT_EXCEL_NULL("06121","当前数据为空，不可导出数据"),


    ;

    private String code;
    private String msg;

    BizExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * 此处可以动态修改msg的返回值
     * 这么做是为了实现自定义异常信息
     *
     * @param msg
     * @return
     */
    @Override
    public BizExceptionEnum definedMsg(String msg) {
        this.msg = msg;
        return this;
    }

}
