package com.rjhc.credit.information.service.server.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
@Data
public class BaseResponse<T> implements Serializable {
    private boolean success;
    private String message;
    private T userId;


}
