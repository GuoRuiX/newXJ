package com.rjhc.credit.information.service.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.param.OrgManagerParam;
import com.rjhc.credit.information.service.server.dao.dataobject.OrgManager;

/**
 * @ClassName OrgManagerService
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
public interface OrgManagerService {

    Page<OrgManager> selectByPage(OrgManagerParam orgManagerParam);
}
