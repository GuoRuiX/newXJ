package com.rjhc.credit.information.service.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.param.WaringPoorParam;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;

/**
 * @ClassName waringPoorService
 * @Description: TODO
 * @Author grx
 * @Date 2020/9/21
 * @Version V1.0
 **/
public interface WaringPoorService {
    Page<WaringPoor> selPage(WaringPoorParam waringPoorParam) throws Exception;
    void delByType(String Type);
    void updateByType(String newType,String oldType);
}
