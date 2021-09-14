package com.rjhc.credit.information.service.server.dao.dataobject;

import com.alibaba.fastjson.JSON;

import java.util.*;

public enum Status {
    SUCCESS("0000", "成功"),//
    ;

    private String code;
    private String msg;

    Status(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 获取所有状态码
     *
     * @return
     */
    public static List<Status> statusList() {
        return Arrays.asList(Status.values());
    }

    /**
     * 获取所有状态
     *
     * @return
     */
    public static List<Map> statusMapList() {
        List<Map> list = new ArrayList<>();
        Status[] values = Status.values();
        for (Status status : values) {
            list.add(new HashMap() {{
                this.put("code", status.getCode());
                this.put("msg", status.getMsg());
            }});
        }
        return list;
    }

    /**
     * 获取所有状态
     *
     * @return
     */
    public static String statusJson() {
        return JSON.toJSONString(statusMapList());
    }

    /**
     * 当前状态
     *
     * @return
     */
    public String json() {
        return JSON.toJSONString(map());
    }

    /**
     * 当前状态
     *
     * @return
     */
    public Map map() {
        String code = this.getCode();
        String msg = this.getMsg();
        return new HashMap() {{
            this.put("code", code);
            this.put("msg", msg);
        }};
    }
}
